import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import datapackage.*;

public class Server {

    private final static int DEATH = 0;
    private final static int ATTACK = 1;
    private final static int ATTACK_COUNTER = 2;
    private final static int DEFEND = 3;
    private final static int DEFEND_COUNTER = 4;
    private final static int DAMAGE_BUFF = 5;
    private final static int BUFF_COUNTER = 6;
    private final static int FINISH = 7;
    static Info info;
    static Data DPlayer;
    static Data DEnemy;
    static Scanner scanner = new Scanner(System.in);
    public static void cls(int n){ for(int i = 0; i < n; i++) System.out.println(); }

    public static void pause() {
        try {
            System.in.read();
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    public static int ChoiceInput(){
        System.out.println("1. ATTACK 2. ATTACK_COUNTER 3. DEFEND 4. DEFNED_COUNTER 5. DAMAGE_BUFF 6. BUFF_COUNTER");
        System.out.print("Select the card you want to use : ");
        int Choice = scanner.nextInt(); // 서버의 선택을 직접 선택함
        return Choice;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        int a = 0;
        int iDamage = 0;
        String strlog = "";
        String strlog2 = "";
        Random random = new Random();
        info = new Info();
        info.Init(); // 서버 플레이어 데이터 초기화
        DPlayer = new Data(); // 나의 데이터
        DPlayer.SetInfo(info);
        DEnemy = new Data(); // 상대방 데이터

        ServerSocket server = new ServerSocket(5000);

        while(true) {
            //int MyChoice = random.nextInt(6)+1; // 서버의 행동을 랜덤으로 설정
            int MyChoice = ChoiceInput();// 서버의 행동을 직접 선택함

            System.out.println("Waiting for the other person's choice... ");
            Socket client = server.accept();

            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            cls(50);
            System.out.println("[Data From Client]: " + ++a);

            // 상대방으로부터 데이터를 받은 후 분기 처리
            DEnemy = (Data) in.readObject();
            switch (MyChoice){
                case ATTACK:
                    DPlayer.SetChoice(ATTACK);
                    break;
                case ATTACK_COUNTER:
                    DPlayer.SetChoice(ATTACK_COUNTER);
                    break;
                case DEFEND:
                    DPlayer.SetChoice(DEFEND);
                    break;
                case DEFEND_COUNTER:
                    DPlayer.SetChoice(DEFEND_COUNTER);
                    break;
                case DAMAGE_BUFF:
                    DPlayer.SetChoice(DAMAGE_BUFF);
                    break;
                case BUFF_COUNTER:
                    DPlayer.SetChoice(BUFF_COUNTER);
                    break;
                case FINISH:
                    DPlayer.SetChoice(FINISH);
                    break;
            }

            out.writeObject(DPlayer);


            // 내가 보낸 데이터에 따른 분기처리
            switch (MyChoice){
                case ATTACK:
                    iDamage = DPlayer.GetInfo().GetDamage();
                    if(DEnemy.GetChoice() == DEFEND)
                        iDamage -= DPlayer.GetInfo().GetArmor();
                    DEnemy.GetInfo().SetHP(iDamage);
                    strlog=iDamage + "Attack.\n";
                    break;
                case ATTACK_COUNTER:// ATTACK_COUNTER todo
                    if(DEnemy.GetChoice() == ATTACK) {
                        DEnemy.GetInfo().SetHP(DEnemy.GetInfo().GetDamage());
                        DPlayer.GetInfo().SetHP(-DEnemy.GetInfo().GetDamage());
                        strlog="Counter for the opponent's attack!\n";
                    }
                    else{
                        strlog="Failed to counter the opponent's attack.\n";
                    }
                    break;
                case DEFEND:// DEFEND
                    DPlayer.GetInfo().SetArmor(3);
                    strlog = DPlayer.GetInfo().GetArmor() + "Attack.\n";
                    break;
                case DEFEND_COUNTER:// DEFEND_COUNTER todo
                    if(DEnemy.GetChoice() == DEFEND){
                        DEnemy.GetInfo().SetHP(DEnemy.GetInfo().GetArmor());
                        strlog = "Counter for the opponent's defense!\n";
                    }
                    else{
                        strlog = "I couldn't counter the opponent's defense.\n";
                    }
                    break;
                case DAMAGE_BUFF:// DAMAGE_BUFF todo
                    DPlayer.GetInfo().SetDamageDouble();
                    DPlayer.GetInfo().SetBuffCount(3);
                    strlog = "Attack power doubles in two turns.\n";
                    break;
                case BUFF_COUNTER:// BUFF_COUNTER todo
                    DEnemy.GetInfo().SetBuffCount(-DEnemy.GetInfo().GetBuffCount());
                    strlog = "You have released the other person's buff!\n";
                    break;
                case FINISH:// FINISH todo
                    DPlayer.SetChoice(FINISH);
                    break;
            }

            // 상대방이 보낸 데이터에 따른 분기처리
            switch (DEnemy.GetChoice()){
                case DEATH:// DEATH을 보냈다는 것은 상대가 죽었다는 것을 의미
                    // 상대방이 죽었을 때의 동작 실행
                    System.out.println("We won!");
                    return;
                case ATTACK:
                    iDamage = DEnemy.GetInfo().GetDamage();
                    if(MyChoice == DEFEND)
                        iDamage -= DPlayer.GetInfo().GetArmor();
                    DPlayer.GetInfo().SetHP(iDamage);
                    strlog = strlog + "You "+ iDamage + "Attack. \n";
                    break;
                case ATTACK_COUNTER:// ATTACK_COUNTER todo
                    if(MyChoice == ATTACK) {
                        DPlayer.GetInfo().SetHP(DPlayer.GetInfo().GetDamage());
                        DEnemy.GetInfo().SetHP(-DPlayer.GetInfo().GetDamage());
                        strlog = strlog + "The other person counted on my attack! \n";
                    }
                    else{
                        strlog = strlog + "My opponent counter my attack! \n";
                    }
                    break;
                case DEFEND:// DEFEND todo
                    DEnemy.GetInfo().SetArmor(3); //의도치 않은 동작을 할 수 있음
                    strlog = strlog + "You " + DEnemy.GetInfo().GetArmor() + "defend .\n";
                    break;
                case DEFEND_COUNTER:// DEFEND_COUNTER todo
                    if(MyChoice == DEFEND){
                        DPlayer.GetInfo().SetHP(DPlayer.GetInfo().GetArmor());
                        strlog = strlog + "My opponent counter my defense!\n";
                    }
                    else{
                        strlog = strlog + "My opponent couldn't counter my defense.\n";
                    }
                    break;
                case DAMAGE_BUFF:// DAMAGE_BUFF todo
                    DEnemy.GetInfo().SetDamageDouble();
                    DEnemy.GetInfo().SetBuffCount(3);
                    strlog = strlog + "The opponent's attack power doubles for two turns.\n";
                    break;
                case BUFF_COUNTER:// BUFF_COUNTER todo
                    DPlayer.GetInfo().SetBuffCount(-DPlayer.GetInfo().GetBuffCount());
                    strlog = strlog + "The other person released my buff!\n";
                    break;
                case FINISH:// FINISH todo
                    break;
            }

            // 버프 체크
            if(DPlayer.GetInfo().GetBuffCount()>0) {
                DPlayer.GetInfo().SetBuffCount(-1);
                if(DPlayer.GetInfo().GetBuffCount() == 0) {
                    DPlayer.GetInfo().SetDamage(5);
                    strlog2 += "Buff has been released.\n";
                }
            }

            if(DEnemy.GetInfo().GetBuffCount()>0) {
                DEnemy.GetInfo().SetBuffCount(-1);
                if(DEnemy.GetInfo().GetBuffCount() == 0) {
                    DEnemy.GetInfo().SetDamage(5);
                    strlog2 += "The other person's buff has been released \n";
                }
            }
            System.out.println(strlog); // 로그 정보 출력
            System.out.println("---------Counterparty Information---------\n" + DEnemy.GetInfo().GetStat()); // 상대방의 정보 출력
            System.out.println("---------My information---------\n" + info.GetStat() + strlog2); // 플레이어 정보와 버프 로그 출력
            // 나의 데이터를 상대방에게 보냄

            if(info.GetHP() <= 0) {// 만약 플레이어의 죽은 상태라면 죽었다고 표시하고 데이터 보냄
                DPlayer.SetChoice(0);
                System.out.println("I lost...");
                out.writeObject(DPlayer);
                client.close();
                return;
            }
            if(DEnemy.GetInfo().GetHP() <= 0) {// 만약 플레이어의 죽은 상태라면 죽었다고 표시하고 데이터 보냄
                DPlayer.SetChoice(0);
                System.out.println("We won!");
                return;
            }

            client.close();

        }


        // try{
        // Socket socket = new Socket("127.0.0.1", 5000);
//
        //          System.out.println("1");
        //        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        //      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//
        //          Data data = new Data();
        //        out.writeObject(data);
        //      System.out.println("2");
//
//
        //          Data d = (Data) in.readObject();
//
        //          System.out.println(d.getAttack());
//
        //      }catch(Exception e){
        //        e.printStackTrace();
        //  }
    }
}


/*
            switch (DEnemy.GetChoice()){
                case ATTACK:
                    //info.SetHP(info.GetHP() - DEnemy.GetInfo().GetDamage());
                    System.out.println("상대방이 5의 데미지로 공격했습니다.");
                    break;
                case ATTACK_COUNTER:
                    System.out.println("상대방이 공격카운터했습니다");
                    break;
                case DEFEND:
                    System.out.println("상대방이 방어했습니다");
                    break;
                case DEFEND_COUNTER:
                    System.out.println("상대방이 방어 카운터했습니다");
                    break;
                case DAMAGE_BUFF:
                    System.out.println("상대방이 버프했습니다");
                    break;
                case BUFF_COUNTER:
                    System.out.println("상대방이 버프카운터했습니다");
                    break;
                case FINISH:
                    System.out.println("상대방이 종료했습니다");
                    break;
            }*/