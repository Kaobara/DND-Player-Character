package staticScrapeService;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.ArrayList;

// Content Table class is a class that contains a table
// With all of its contents formatted to be easily read as text to be sent on Discord
public class ContentTable {
    final private ArrayList<ArrayList<String>> rowContents = new ArrayList<>();
    final private ArrayList<ArrayList<String>> headers = new ArrayList<>();
    final private ArrayList<String> finalTableContent = new ArrayList<>();
    final private String finalTable;
    private boolean tableInitialized = false;
    private final int numRows;
    private int numColumns =1;
    private final int FINAL_CELL_SIZE = 20;

    // Create "Content Table
    public ContentTable(HtmlTable table) {
        numRows = table.getRowCount();

        boolean firstLoop = true;

        for (final HtmlTableRow row : table.getRows()) {
            // Deal with Headers (might make into its own method
            DomNodeList<DomNode> nodes = row.querySelectorAll("th");
            if(!nodes.isEmpty()) {
                ArrayList<String> currentHeaderRow = new ArrayList<>();
                for (DomNode node : nodes) {
                    currentHeaderRow.add(node.getTextContent());
                }
                headers.add(currentHeaderRow);
                continue;
            }

            // Every other row
            ArrayList<String> currentRow = new ArrayList<>();

            // Cell per row
            for(final HtmlTableCell cell : row.getCells()) {
                // Count the number of columns of the table by only the first row
                if(firstLoop) {
                    numColumns++;
                }
                currentRow.add(cell.getTextContent());
            }
            if(firstLoop) {
                firstLoop = false;
            }
            rowContents.add(currentRow);
        }
        finalTable = makeFormattedTable();
    }

    // Format the table into a single string to be "Discord-text/Markdown friendly"
    // Add ``` to start and end of String to format it as a "Code Block"
    private String makeFormattedTable() {
        StringBuilder finalTable = new StringBuilder("```");
        // Header
        for(ArrayList<String> header : headers) {
            finalTableContent.add(formatRow(header));
        }

        // Divider between headers and rest of text
        finalTableContent.add(addDivider());

        // The rest of the table
        for(ArrayList<String> rows : rowContents) {
            finalTableContent.add(formatRow(rows));
        }

        // Append new lines to all rows. This might be a bit inefficient, might make better later
        for(String row : finalTableContent) {
            finalTable.append(row).append("\n");
        }

        tableInitialized = true;
        return finalTable + "```";
    }

    // Format a row to have dividers between columns (|)
    private String formatRow(ArrayList<String> row) {
        StringBuilder finalRow = new StringBuilder("| ");
        for(String cell : row ) {
            if(cell.isEmpty()) {
                continue;
            }
            finalRow.append(cell);

            // To make sure that more cells are of the same length
            // populate the string per cell with spaces
            // This block might change to not be based on a final cell size.
            for(int i = cell.length(); i<FINAL_CELL_SIZE; i++) {
                finalRow.append(" ");
            }
            finalRow.append("|");
        }
        return finalRow.toString();
    }

    // Divider between headers and text
    private String addDivider() {
        StringBuilder divider = new StringBuilder("| ");
        for(int i = 1; i<numColumns; i++) {
            for(int j = 0; j<FINAL_CELL_SIZE; j++) {
                divider.append("-");
            }
            divider.append("+");
        }
        return divider.toString();

    }

    // Return the Discord-Text/Markdown table.
    public String getFullTable() {
        if (!tableInitialized) {
            return "";
        }
        return finalTable;
    }


}
