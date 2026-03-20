package shop.util;

import shop.model.Employee;

/**
 * Singleton that stores the currently logged-in user for the session.
 */
public class AppSession {
    private static final AppSession INSTANCE = new AppSession();
    private Employee currentUser;

    private AppSession() {}

    public static AppSession getInstance() {
        return INSTANCE;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Employee user) {
        this.currentUser = user;
    }

    public boolean isOwner() {
        return currentUser != null && "OWNER".equalsIgnoreCase(currentUser.getRole());
    }

    public void logout() {
        currentUser = null;
    }
}
