package datapackage;

import java.io.Serializable;
public class Info implements Serializable{

    private int m_iHP;
    private int m_iBuffCount;
    protected int m_iDamage;
    protected int m_iArmor;

    public int GetHP() {
        return m_iHP;
    }

    public int GetDamage() { return m_iDamage; }

    public void SetDamage(int iDamage) {this.m_iDamage = iDamage;}
    public void SetDamageDouble(){this.m_iDamage*=2;}

    public int GetArmor() {return m_iArmor;}

    public void SetArmor(int iArmor) {this.m_iArmor = iArmor;}

    public void SetHP(int iHP) {this.m_iHP -= iHP;}
    public int GetBuffCount() {return m_iBuffCount;}

    public void SetBuffCount(int buffCount) {m_iBuffCount += buffCount;}

    public String GetStat(){
        String str = "남은 체력 :" + m_iHP + "\n"
                + "공격력 계수 : " + m_iDamage + "\n"
                + "방어력 계수 : " + m_iArmor + "\n"
                + "버프 지속시간 : " + m_iBuffCount + "\n";
        return str;
    }

    public void Init() {
        this.m_iHP = 30;
        this.m_iDamage = 5;
        this.m_iArmor = 3;
        this.m_iBuffCount = 0;
    }
}

