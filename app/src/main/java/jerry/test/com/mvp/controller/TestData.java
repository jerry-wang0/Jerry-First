package jerry.test.com.mvp.controller;

/**
 * Created by jerry on 2017/05/11.
 */

public class TestData {
    public enum StatusCode {
        OK(100), // 100 OK
        NO_LOGIN(101), // 101 Not Login
        INVALID_ACCESS(102), //
        NETWORK_ERROR(198),//network error
        UNKNOWN_ERROR(199), // 199 Unknown error
        UNCONNECT_TO_SERVER(999); // 999 Cannot connect to server

        final int status;

        private StatusCode(int status) {
            this.status = status;
        }

        public static StatusCode convert(int status) {
            for (StatusCode sc : StatusCode.values()) {
                if (sc.status == status) {
                    return sc;
                }
            }
            return UNKNOWN_ERROR;
        }
    }

    private StatusCode status = StatusCode.UNKNOWN_ERROR;
    private int ticketNumYour = 0;
    private int ticketNumMax = 0;
    private int yamawakeToday = 0;
    private String userName = "";
    private int toolId = 0;
    private String time = "";
    private String keyWord = "";
    private String redirectUrl = "";

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }

    public int getTicketNumYour() {
        return ticketNumYour;
    }

    public void setTicketNumYour(int ticket_num_your) {
        this.ticketNumYour = ticket_num_your;
    }

    public int getTicketNumMax() {
        return ticketNumMax;
    }

    public void setTicketNumMax(int ticket_num_max) {
        this.ticketNumMax = ticket_num_max;
    }

    public int getYamawakeToday() {
        return yamawakeToday;
    }

    public void setYamawakeToday(int yamawake_today) {
        this.yamawakeToday = yamawake_today;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user_name) {
        this.userName = user_name;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int tool_id) {
        this.toolId = tool_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("status : ").append(status.name())
                .append(" ticket_num_your : ").append(ticketNumYour)
                .append(" ticket_num_max : ").append(ticketNumMax)
                .append(" yamawake_today : ").append(yamawakeToday)
                .append(" user_name : ").append(userName).append(" tool_id : ")
                .append(toolId).append(" time : ").append(time)
                .append("keyWord : ").append(keyWord)
                .append("redirect_url : ").append(redirectUrl).toString();
    }
}
