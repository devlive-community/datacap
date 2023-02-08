package io.edurt.datacap.plugin.natived.zookeeper;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.plugin.natived.zookeeper.sql.ZookeeperCaseInsensitiveStream;
import io.edurt.datacap.plugin.natived.zookeeper.sql.ZookeeperSql;
import io.edurt.datacap.plugin.natived.zookeeper.sql.ZookeeperSqlLexer;
import io.edurt.datacap.plugin.natived.zookeeper.sql.ZookeeperSqlParser;
import io.edurt.datacap.plugin.natived.zookeeper.sql.ZookeeperSqlVisitor;
import io.edurt.datacap.spi.adapter.NativeAdapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", "REC_CATCH_EXCEPTION"},
        justification = "I prefer to suppress these FindBugs warnings")
public class ZookeeperAdapter
        extends NativeAdapter
{
    protected ZookeeperConnection zookeeperConnection;

    public ZookeeperAdapter(ZookeeperConnection zookeeperConnection)
    {
        super(zookeeperConnection);
        this.zookeeperConnection = zookeeperConnection;
    }

    @Override
    public Response handlerExecute(String content)
    {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.zookeeperConnection.getResponse();
        Configure configure = this.zookeeperConnection.getConfigure();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                CharStream stream = CharStreams.fromString(content);
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
                    ZookeeperSql node = simpleSqlTreeVisitor.visit(tree);
                    Preconditions.checkArgument(node.isSupport(), String.format("Not support type <%s>", node.getToken()));

                    ZkClient client = this.zookeeperConnection.getClient();
                    headers.add(node.getColumns().get(0));
                    types.add("String");
                    client.getChildren(ZookeeperPathConvert.toPath(node.getTable()))
                            .forEach(column -> columns.add(handlerFormatter(configure.getFormat(), headers, Collections.singletonList(column))));
                    response.setIsSuccessful(Boolean.TRUE);
                }
            }
            catch (Exception ex) {
                log.error("Execute content failed content {} exception ", content, ex);
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            }
            finally {
                response.setHeaders(headers);
                response.setTypes(types);
                response.setColumns(columns);
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }
}
