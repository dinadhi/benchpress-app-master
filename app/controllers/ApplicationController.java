package controllers;

import actors.ApplicationActor;
import actors.ApplicationActorProtocol;
import actors.ApplicationActorProtocol.Response;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import components.WSComponent;
import models.Benchpress;
import models.Stats;
import play.libs.Json;
import play.libs.concurrent.Futures;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import repository.BenchpressRepository;
import repository.StatsRepository;
import scala.compat.java8.FutureConverters;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class ApplicationController extends Controller {

    private final ActorRef appActor;
    private final WSComponent ws;
    private final StatsRepository statsRepo;
    private final BenchpressRepository bpRepo;
    private HttpExecutionContext httpExecutionContext;

    @Inject
    public ApplicationController(
            ActorSystem system,
            HttpExecutionContext ec,
            WSComponent ws,
            StatsRepository statsRepo,
            BenchpressRepository bpRepo) {
        appActor = system.actorOf(ApplicationActor.getProps().withRouter(new RoundRobinPool(100)));
        this.httpExecutionContext = ec;
        this.ws = ws;
        this.statsRepo = statsRepo;
        this.bpRepo = bpRepo;
    }

    /**
     * An action that renders an Json data.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(Json.toJson(bpRepo.list()));
    }

    /**
     * An action that process URL for given parallelism factor and renders the Json data.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/benchmark</code>.
     *
     * @param path        The URL Path
     * @param parallelism The parallelism factor
     * @return
     */
    public CompletionStage<Result> benchmark(String path, Long parallelism) {
        System.out.println("Path : " + path + " parallelism : " + parallelism);
        Stream<CompletionStage<Response>> promises = parallelismStream(parallelism).map(count -> {
            return FutureConverters.toJava(ask(appActor, new ApplicationActorProtocol.Process(path, ws), 10000))
                    .thenApply(wsResponse -> (Response) wsResponse);
        });

        return Futures.sequence(promises.collect(Collectors.toList())).thenApply(result -> {
            Benchpress benchpress = new Benchpress();
            benchpress.parallelism = parallelism;
            benchpress.url = path;
            benchpress.id = bpRepo.insert(benchpress);
            result.forEach(data -> {
                System.out.println("Status : " + data.status + " Time : " + data.timeTaken);
                Stats stats = new Stats();
                stats.timeTaken = data.timeTaken;
                stats.status = data.status;
                stats.benchpress = benchpress;
                statsRepo.insert(stats);
            });
            return ok(Json.toJson(benchpress));
        });
    }

    /**
     * An action that renders the list of stats by benchpress id as Json data.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/stats</code>.
     *
     * @param id The Benchpress Id
     */
    public CompletionStage<Result> stats(Long id) {
        return supplyAsync(() -> {
            List<Stats> stats = statsRepo.findByBenchpressId(id);
            return ok(Json.toJson(stats));
        }, httpExecutionContext.current());
    }

    /**
     * An action that renders the all list of stats as Json data.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/all/stats</code>.
     */
    public CompletionStage<Result> allStats() {
        return supplyAsync(() -> {
            List<Stats> stats = statsRepo.list();
            return ok(Json.toJson(stats));
        }, httpExecutionContext.current());
    }

    /**
     * Create parallelism stream on the basis of parallelism factor
     *
     * @param parallelism
     * @return
     */
    private Stream<Long> parallelismStream(Long parallelism) {
        List<Long> parallelismList = new ArrayList<>();
        for (long i = 1; i <= parallelism; i++) {
            parallelismList.add(i);
        }
        return parallelismList.stream();
    }

}
