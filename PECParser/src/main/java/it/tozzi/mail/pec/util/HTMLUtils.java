package it.tozzi.mail.pec.util;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import emoji4j.EmojiUtils;

/**
 * 
 * @author biagio.tozzi
 *
 */
public class HTMLUtils {

	/**
	 * Converte i caratteri unicode contenuti nell'html in html
	 * 
	 * @param html
	 * @return html senza caratteri unicode
	 */
	public static String unescapeEmoji(String html) {
		if (EmojiUtils.countEmojis(html) > 0) {
			return EmojiUtils.htmlify(html);
		}

		return html;
	}

	/**
	 * Converte l'html in testo
	 * 
	 * @param html
	 * @return text
	 */
	public static String htmlToText(String html) {
		return getPlainText(Jsoup.parse(html));
	}

	/**
	 * FROM:
	 * https://github.com/jhy/jsoup/blob/master/src/main/java/org/jsoup/examples/HtmlToPlainText.java
	 * 
	 * @author Jonathan Hedley, jonathan@hedley.net
	 */

	private static String getPlainText(Element element) {
		FormattingVisitor formatter = new FormattingVisitor();
		NodeTraversor.traverse(formatter, element);
		String txt = formatter.toString();
		txt = StringEscapeUtils.unescapeHtml4(txt);
		txt = StringEscapeUtils.unescapeXml(txt);
		return txt;
	}

	private static class FormattingVisitor implements NodeVisitor {
		private static final int maxWidth = 80;
		private int width = 0;
		private StringBuilder accum = new StringBuilder();

		@Override
		public void head(Node node, int depth) {
			String name = node.nodeName();
			if (node instanceof TextNode) {
				append(((TextNode) node).text());
			} else if (name.equals("li")) {
				append("\n * ");
			} else if (name.equals("dt")) {
				append("  ");
			} else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr")) {
				append("\n");
			}
		}

		@Override
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5")) {
				append("\n");
			} else if (name.equals("a")) {
				append(String.format(" <%s>", node.absUrl("href")));
			}
		}

		private void append(String text) {
			if (text.startsWith("\n")) {
				width = 0;
			}
			if (text.equals(" ")
					&& (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n"))) {
				return;
			}

			if (text.length() + width > maxWidth) {
				String[] words = text.split("\\s+");
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					boolean last = i == words.length - 1;
					if (!last) {
						word = word + " ";
					}
					if (word.length() + width > maxWidth) {
						accum.append("\n").append(word);
						width = word.length();
					} else {
						accum.append(word);
						width += word.length();
					}
				}
			} else {
				accum.append(text);
				width += text.length();
			}
		}

		@Override
		public String toString() {
			return accum.toString();
		}
	}

}
