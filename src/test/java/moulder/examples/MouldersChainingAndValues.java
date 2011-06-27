package moulder.examples;

import moulder.values.*;
import moulder.MoulderShop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static moulder.moulds.Moulds.*;

public class MouldersChainingAndValues {
	private static final String HTML = "<html><body><h1>[...]</h1></body></html>";

	@Before
	public void before() {
		System.out.println("\n----------------\n");
	}

	/**
	 * Shows how to chain moulders to achieve complex transformations on
	 * elements. Output:
	 * 
	 * <pre>
	 * <code>
	 * <html>
	 *  <head>
	 *  </head>
	 *  <body> 
	 *   <h1 class="title">title's text</h1> 
	 *   <p>content</p> 
	 *   <h1 class="title">title's text</h1> 
	 *   <p>content</p> 
	 *   <h1 class="title">title's text</h1> 
	 *   <p>content</p> 
	 *   <h1 class="title">title's text</h1> 
	 *   <p>content</p>
	 *  </body>
	 * </html>
	 * </code>
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testChaining() throws Exception {
		Document doc = Jsoup.parse(HTML);
		MoulderShop m = new MoulderShop();

		m.register("h1",
				repeat(Arrays.asList("Spring", "Summer", "Autumn", "Winter")),
				attr("class", "title"), text("title's text"),
				append("<p>content</p>"));

		m.process(doc);

		System.out.println(doc);
	}

	/**
	 * Shows how to a moulder can pass values between each other and how to
	 * values can be used to control the moulders behaviour. Output:
	 * 
	 * <pre>
	 * <code>
	 * <html>
	 *  <head>
	 *  </head>
	 *  <body> 
	 *   <h1 class="even">Spring</h1> 
	 *   <p>Season: <em>Spring</em></p> 
	 *   <h1 class="odd">Summer</h1> 
	 *   <p>Season: <em>Summer</em></p> 
	 *   <h1 class="even">Autumn</h1> 
	 *   <p>Season: <em>Autumn</em></p> 
	 *   <h1 class="odd">Winter</h1> 
	 *   <p>Season: <em>Winter</em></p>
	 *  </body>
	 * </html>
	 * </code>
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testValues() throws Exception {
		Document doc = Jsoup.parse(HTML);
		MoulderShop m = new MoulderShop();

		m.register("h1", repeat(Arrays.asList("Spring", "Summer", "Autumn",
				"Winter")),
				attr("class", new Values<String>("even", "odd").cycle()),
				text(new ElementDataValue<String>()), append(new HtmlValue(
						new ValueTransformer<String, String>(
								new ElementDataValue<String>()) {

							@Override
							protected String transform(String s) {
								return "<p>Season: <em>" + s + "</em></p>";
							}
						})));

		m.process(doc);

		System.out.println(doc);
	}

	public static class Tag {
		private String tag;
		private int length;

		public Tag(String tag) {
			super();
			this.tag = tag;
			this.length=tag.length();
		}

		public String getTag() {
			return tag;
		}
		public int getLength() {
			return length;
		}
	}
	
	@Test
	public void testValues2() throws Exception {
		Document doc = Jsoup.parse(HTML);
		MoulderShop m = new MoulderShop();
		
		m.register("h1", repeat(Arrays.asList(new Tag("Java"),
				new Tag("jQuery"), new Tag("templating"))),
				attr("class", new Values<String>("even", "odd").cycle()),
				text(new ValueFieldExtractor<String, Tag>(new ElementDataValue<Tag>(),
						"tag", Tag.class)), append(new HtmlValue(
						new ToStringValue<Integer>(
								new ValueFieldExtractor<Integer, Tag>(
										new ElementDataValue<Tag>(),
										"length", Tag.class)))));

		m.process(doc);

		System.out.println(doc);
	}
}