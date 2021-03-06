package main;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is not a Java compiler.
 * This is just a Java parser, so it assumes that the Java code is valid.
 */
class JavaParser {

    private static final Pattern EMPTY_OR_WHITESPACES = Pattern.compile("^\\s*$");
    private static final Pattern DOUBLE_FORWARD_SLASHES_COMMENT = Pattern.compile("^\\s*//");
    private static final Pattern MULTILINE_COMMENT_BEGINNING = Pattern.compile("^\\s*/\\*{1,2}");
    private static final Pattern MULTILINE_COMMENT_ENDING = Pattern.compile("^.*?\\*/");

    private String resourceFilename;

    private boolean isInMultilineCommentRegion = false;

    JavaParser (final String resourceFilename) {
        this.resourceFilename = resourceFilename;
    }

    List<String> findCodeLines () {
        final List<String> lines = MainUtils.readLinesFromResources(resourceFilename);
        if (lines == null) {
            return null;
        }

        final List<String> codeLines = new ArrayList<>();

        for (String line : lines) {
            if (containsCode(line)) {
                codeLines.add(line);
            }
        }

        return codeLines;
    }

    private boolean containsCode (final String line) {
        // Check if it is an empty line or just tabs and spaces
        if (EMPTY_OR_WHITESPACES.matcher(line).find()) {
            return false;
        }

        // Check if it starts with forward slashes comment
        if (DOUBLE_FORWARD_SLASHES_COMMENT.matcher(line).find()) {
            return false;
        }

        // Check if it is part of a multiline comment
        final Matcher multilineCommentBeginningMatcher = MULTILINE_COMMENT_BEGINNING.matcher(line);
        final boolean multilineCommentBeginningFound = multilineCommentBeginningMatcher.find();
        if (isInMultilineCommentRegion || multilineCommentBeginningFound) {
            // Update the flag of multiline comment
            isInMultilineCommentRegion = true;
            // Get the beginning of the multiline comment (index)
            final int multilineCommentBeginningIndex = multilineCommentBeginningFound ?
                multilineCommentBeginningMatcher.start() : 0;
            // Check if the comment also ends on the same line (start the search from the previous index)
            final Matcher multilineCommentEndingMatcher = MULTILINE_COMMENT_ENDING.matcher(line);
            if (multilineCommentEndingMatcher.find(multilineCommentBeginningIndex)) {
                // Update the flag of multiline comment again
                isInMultilineCommentRegion = false;
                // Check the rest of the line
                final String remainingLine = multilineCommentEndingMatcher.replaceAll("");
                return containsCode(remainingLine);
            }
            return false;
        }

        return true;
    }

}
