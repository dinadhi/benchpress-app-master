package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Benchpress;
import models.Stats;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class BenchpressRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public BenchpressRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public List<Benchpress> list() {
        return ebeanServer.find(Benchpress.class).findList();
    }

    public Benchpress findById(Long id) {
        return ebeanServer.find(Benchpress.class).setId(id).findUnique();
    }

    public Long insert(Benchpress benchpress) {
        benchpress.id = System.currentTimeMillis(); // not ideal, but it works
        ebeanServer.insert(benchpress);
        return benchpress.id;
    }

}