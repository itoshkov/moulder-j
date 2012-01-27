package moulder.moulds;

import moulder.ElementAndData;
import moulder.NodeAndData;
import moulder.Value;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.StringReader;
import java.util.List;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.mockito.Mockito.*;

public class AppenderTest extends BaseMoulderTest {
	

	@Test
	public void test() throws Exception {
		Value<Iterable<Node>> content = mock(Value.class);
		when(content.get()).thenReturn(parse("<e a='v'>c</e>text"));

		Appender a = new Appender(content);
		Document document = Jsoup
				.parseBodyFragment("<html><body><outer>test</outer></body></html>");
		Element element = document.getElementsByTag("outer").first();

		ElementAndData nd = new ElementAndData(element, "data");
		List<NodeAndData> processed = a.process(nd);

		// verify that bind and get were called, in this order
		InOrder inOrder = inOrder(content);
		inOrder.verify(content).bind(nd);
		inOrder.verify(content).get();

		assertXMLEqual(new StringReader(
				"<body><outer>test</outer><e a='v'>c</e>text</body>"),
				new StringReader(html(processed)));
	}

	
}
