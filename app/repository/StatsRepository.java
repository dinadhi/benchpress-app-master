package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Stats;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class StatsRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public StatsRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public List<Stats> list() {
        return ebeanServer.find(Stats.class).findList();
    }

    public List<Stats> findByBenchpressId(Long benchpressId) {
        return ebeanServer.find(Stats.class).where().eq("benchpress_id", benchpressId).findList();
    }

    public Long insert(Stats stats) {
        stats.id = System.currentTimeMillis(); // not ideal, but it works
        ebeanServer.insert(stats);
        return stats.id;
    }

}