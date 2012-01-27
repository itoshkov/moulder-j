package moulder.moulds;

import moulder.ElementAndData;
import moulder.Moulder;
import moulder.NodeAndData;
import moulder.Value;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.mockito.Mockito.*;

public class IfMoulderTest extends BaseMoulderTest {

	@Test
	public void testThen() throws Exception {		
		test(true, "<body><b>text</b>text</body>");
	}
	
	@Test
	public void testElse() throws Exception {		
		test(false, "<body><c>content</c>text</body>");
	}
	
	private void test(boolean res, String expected) throws Exception {
		Value<Boolean> condition = mock(Value.class);
		when(condition.get()).thenReturn(res);

		Document document = Jsoup
				.parseBodyFragment("<html><body><outer a='v'><a>test</a></outer></body></html>");
		Element element = document.getElementsByTag("outer").first();
		ElementAndData nd = new ElementAndData(element, "data");

		Moulder thenMoulder = mock(Moulder.class);
		ArgumentCaptor<ElementAndData> ifMoulderCaptor = ArgumentCaptor
				.forClass(ElementAndData.class);
		when(
				thenMoulder.process(ifMoulderCaptor.capture())).thenReturn(
				Arrays.asList(new NodeAndData(parseNode("<b>text</b>")),
						new NodeAndData(parseNode("text"))));

		Moulder elseMoulder = mock(Moulder.class);
		ArgumentCaptor<ElementAndData> elseMoulderCaptor = ArgumentCaptor
				.forClass(ElementAndData.class);
		when(
				elseMoulder.process(elseMoulderCaptor.capture())).thenReturn(
				Arrays.asList(new NodeAndData(parseNode("<c>content</c>")),
						new NodeAndData(parseNode("text"))));

		IfMoulder a = new IfMoulder(condition, thenMoulder, elseMoulder);

		List<NodeAndData> processed = a.process(nd);

		// verify that bind and get were called, in this order
		InOrder inOrder = inOrder(condition);
		inOrder.verify(condition).bind(nd);
		inOrder.verify(condition).get();

		// check that the sub moulder called if ifMoulder and returned its
		// result
		assertXMLEqual(new StringReader(expected),
				new StringReader(html(processed)));
	}

}
