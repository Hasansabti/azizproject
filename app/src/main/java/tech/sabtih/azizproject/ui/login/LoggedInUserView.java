package tech.sabtih.azizproject.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private boolean renter = false;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, boolean renter)
    {
        this.renter = renter;

        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    public boolean isRenter() {
        return renter;
    }
}
