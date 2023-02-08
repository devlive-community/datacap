package io.edurt.datacap.plugin.natived.zookeeper.sql;

import com.google.common.base.Preconditions;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

public class ZookeeperSqlParserTest
{
    public ZookeeperSql getConfigure(String sql)
    {
        CharStream stream = CharStreams.fromString(sql);
        ZookeeperSqlLexer lexer = new ZookeeperSqlLexer(new ZookeeperCaseInsensitiveStream(stream));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ZookeeperSqlParser parser = new ZookeeperSqlParser(tokens);
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.removeErrorListeners();

        ParseTree tree = null;
        try {
            tree = parser.singleStatement();
        }
        catch (Exception ex) {
            Preconditions.checkArgument(false, "Not support this sql");
        }

        ZookeeperSqlVisitor simpleSqlTreeVisitor = new ZookeeperSqlVisitor();
        if (ObjectUtils.isNotEmpty(tree)) {
            ZookeeperSql configure = simpleSqlTreeVisitor.visit(tree);
            return configure;
        }
        return null;
    }

    @Test
    public void testSelect()
    {
        String sql = "SELECT * FROM test.dd.dd";
        ZookeeperSql configure = getConfigure(sql);
        Assert.assertEquals(configure.getToken(), ZookeeperSqlToken.SELECT);
        Assert.assertTrue(configure.getColumns().size() > 0);
        Assert.assertNotNull(configure.getTable());

        sql = "Select * FROM test.dd.`dd`";
        configure = getConfigure(sql);
        Assert.assertNotNull(configure.getTable());
    }
}
