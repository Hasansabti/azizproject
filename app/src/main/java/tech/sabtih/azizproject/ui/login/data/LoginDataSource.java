package tech.sabtih.azizproject.ui.login.data;

import tech.sabtih.azizproject.ui.login.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe",false);

            if(username.equals("sp")){
                fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username,false);
            }else if(username.equals("renter")){
                fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username,true);
            }
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
