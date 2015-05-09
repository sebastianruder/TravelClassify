import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import cc.mallet.classify.Classifier;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

import java.io.PrintWriter;
import java.util.List;

public class Main extends HttpServlet {

    private static MongoDBHandler handler;
    private static DocumentClassifier docClassifier;
    private static Classifier classifier;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

        if (req.getRequestURI().contains("/new_job?")) {
            String userId = req.getRequestURI().split("/new_job?")[1];
            System.out.println(userId);
            resp.getWriter().print("400. Successful. User posts are being analyzed.");
            classifyPosts(userId);
        }
        else {
            showHome(req,resp);
        }
    }

    private void classifyPosts(String userId) throws IOException {
        List<String> posts = handler.getPosts(userId);
        String tempFile = String.format("%s.temp", userId);
        PrintWriter writer = new PrintWriter(tempFile, "UTF-8");
        for (String post : posts) {
            writer.printf("0\t\t%s", post);
        }

        writer.close();

        docClassifier.printLabelings(classifier, new File(tempFile));
    }

    private void showHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Hello from Java!");
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Main()), "/*");
        handler = new MongoDBHandler();
        docClassifier = new DocumentClassifier();
        String classifierFile = "activities.classifier";
        classifier = docClassifier.loadClassifier(new File(classifierFile));
        server.start();
        server.join();
    }
}
