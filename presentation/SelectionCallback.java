package presentation;

public interface SelectionCallback {
    void onSelectionComplete(String p1Flavor, String p1Diff, String p2Flavor, String p2Diff, String p1Name,
            String p2Name);
}
