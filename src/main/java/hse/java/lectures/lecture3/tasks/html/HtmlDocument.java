package hse.java.lectures.lecture3.tasks.html;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HtmlDocument {

    private static final Map<String, String> allowedTags = Map.of(
            "<html>", "</html>",
            "<head>", "</head>",
            "<body>", "</body>",
            "<div>", "</div>",
            "<p>", "</p>"
    );

    public HtmlDocument(String filePath) {
        this(Path.of(filePath));
    }

    public HtmlDocument(Path filePath) {
        String content = readFile(filePath);
        validate(content);
    }

    private String readFile(Path filePath) {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    private String ignoreAttributes(String tag) {
        String lowerTag = tag.toLowerCase();
        for (String allowed : allowedTags.keySet()) {
            var expected = allowed.substring(1, allowed.length() - 1);
            if (lowerTag.startsWith("<" + expected)) {
                return "<" + expected + ">";
            }
            if (lowerTag.startsWith("</" + expected)) {
                return "</" + expected + ">";
            }
        }
        return null;
    }

    private List<String> extractRootTags(String content) {
        List<String> rootTags = new ArrayList<>();
        int pos = 0;
        int depth = 0;

        while (pos < content.length()) {
            int open = content.indexOf('<', pos);
            if (open == -1) break;

            int close = content.indexOf('>', open);
            if (close == -1) break;

            String tag = content.substring(open, close + 1).toLowerCase();
            pos = close + 1;

            if (tag.startsWith("</")) {
                depth--;
            } else if (tag.startsWith("<") && !tag.startsWith("</")) {
                String normalized = ignoreAttributes(tag);
                if (normalized == null || !allowedTags.containsKey(normalized)) {
                    depth++;
                    continue;
                }

                if (depth == 0) {
                    rootTags.add(normalized);
                }
                depth++;
            }
        }

        return rootTags;
    }

    private void validateStructure(String content) {
        int htmlOpenPos = content.indexOf("<html>");
        int htmlClosePos = content.indexOf("</html>");

        if (htmlOpenPos == -1 || htmlClosePos == -1 || htmlOpenPos >= htmlClosePos) {
            throw new InvalidStructureException("Html tag is needed and must be properly opened before closed.");
        }

        for (String openTag : allowedTags.keySet()) {
            checkTagPosition(content, openTag, htmlOpenPos, htmlClosePos);
        }
        for (String closeTag : allowedTags.values()) {
            checkTagPosition(content, closeTag, htmlOpenPos, htmlClosePos);
        }

        String innerHtml = content.substring(htmlOpenPos + "<html>".length(), htmlClosePos);
        List<String> rootTags = extractRootTags(innerHtml);

        if (rootTags.size() != 2) {
            throw new InvalidStructureException("Inside <html> there must be exactly <head> followed by <body>.");
        }

        if (!"<head>".equals(rootTags.get(0))) {
            throw new InvalidStructureException("<head> must be the first child of <html>.");
        }

        if (!"<body>".equals(rootTags.get(1))) {
            throw new InvalidStructureException("<body> must follow <head> as second child of <html>.");
        }
    }

    private void checkTagPosition(String content, String tag, int htmlOpenPos, int htmlClosePos) {
        int index = -1;
        while ((index = content.indexOf(tag, index + 1)) != -1) {
            if (index < htmlOpenPos || index > htmlClosePos) {
                throw new InvalidStructureException(
                        "Tag '" + tag + "' must be inside <html>...</html>, but found at position " + index);
            }
        }
    }

    private void validate(String content) {
        Stack<String> stack = new Stack<>();
        int pos = 0;

        while (pos < content.length()) {
            int tagStart = content.indexOf('<', pos);
            int tagEnd = content.indexOf('>', tagStart);
            if (tagStart == -1 || tagEnd == -1) break;

            String tag = content.substring(tagStart, tagEnd + 1);

            var normalizedTag = ignoreAttributes(tag);

            if (normalizedTag == null) {
                throw new UnsupportedTagException(tag);
            }

            if (allowedTags.containsKey(normalizedTag)) {
                stack.push(normalizedTag.toLowerCase());
            } else if (normalizedTag.startsWith("</")) {
                if (stack.isEmpty()) {
                    throw new UnexpectedClosingTagException(tag);
                }
                String expectedTag = allowedTags.get(stack.peek());
                if (!normalizedTag.equalsIgnoreCase(expectedTag)) {
                    throw new MismatchedClosingTagException(stack.peek());
                }
                stack.pop();
            } else {
                throw new UnsupportedTagException(normalizedTag);
            }

            pos = tagEnd + 1;
        }

        if (!stack.isEmpty()) {
            throw new UnclosedTagException("Unclosed tag: " + stack.peek());
        }

        validateStructure(content.toLowerCase());
    }

}
