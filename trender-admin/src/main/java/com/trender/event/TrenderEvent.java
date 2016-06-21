package com.trender.event;

import com.trender.component.AdminViewType;

/**
 * Created by Egor.Veremeychik on 21.06.2016.
 */
public  abstract class TrenderEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static final class PostViewChangeEvent {
        private final AdminViewType view;

        public PostViewChangeEvent(final AdminViewType view) {
            this.view = view;
        }

        public AdminViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

}