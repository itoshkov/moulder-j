package moulder.moulds;

import moulder.Moulder;
import moulder.Value;
import moulder.values.SimpleValue;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * A moulder that sets its input element text content
 *
 * @author jawher
 */
public class Texter implements Moulder {
    private Value<?> text;

    /**
     * @param text a value that returns the text to be used to set its input
     *             element's content
     */
    public Texter(Value<?> text) {
        this.text = text;
    }

    /**
     * A convenience version of {@link #Texter(Value)} that wraps its argument
     * in a {@link SimpleValue}
     *
     * @param text
     */
    public Texter(String text) {
        this(new SimpleValue<String>(text));
    }

    public List<Node> process(Element element) {
        List<Node> res = new ArrayList<Node>();
        Object t = this.text.get();
        element.text(t == null ? null : t.toString());
        res.add(element);
        return res;
    }

}
