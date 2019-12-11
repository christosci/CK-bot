// @author Christos Kokkinomagoulos

public class Main {

    public static void main(String[] args) throws Exception {
        CKbot ckbot = new CKbot();

        ckbot.setVerbose(true);
        ckbot.connect("irc.freenode.net");
        ckbot.joinChannel("#ck_bot");
    }
}