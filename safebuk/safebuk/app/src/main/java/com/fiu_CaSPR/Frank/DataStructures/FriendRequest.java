package com.fiu_CaSPR.Frank.DataStructures;

/**
 * Created by frankhu on 6/15/15.
 */
public class FriendRequest implements Comparable<FriendRequest>{
    private int score = 0;
    private String name = "";
    private String id = "";
    private double percent = 0.0;
    private String div = "";
    private String mobileDiv = "";
    private int numberInPage = 0;
    private boolean injected = false;

    public String getDangerScore(){
        if(score >15){
            return "red";
        }else if (score > 8){
            return "yellow";
        }else return "green";
    }
    public int getPerecentScore(){
       return score/21;
    }


    public void setPhotoNumber(int photos){
        if(photos == 4){
            score += 0;
        }else if (photos > 3){
            score += 3;
        }else{
            score += 5;
        }
    }

    public void hasInfo(boolean isTrue){
        if(!isTrue)
            score+=5;
    }

    public void numberOfMutualFriends(int num){
        if(num<3){
            score+=5;
        }else if(num < 5){
            score+=3;
        }else{
            // Alot of mutual friends, seems good.
        }
    }

    public void numberOfLifeEvents(int num){
        if(num >5){

        }else if ( num > 3){
            score += num;
        }else{
            score +=6;
        }
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public String getDiv(){return div;}
    public String getMobileDiv(){return mobileDiv;}
    public boolean getInjected(){return injected;}
    public int getNumberInPage(){return numberInPage;}
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDiv(String div){this.div = div;}
    public void setMobileDiv(String mobileDiv){this.mobileDiv = mobileDiv;}
    public void setNumberInPage(int num){numberInPage = num;}
    public void setInjected(boolean injection){injected=injection;}


    @Override
    public int compareTo(FriendRequest f){
        if(this.getDangerScore().equalsIgnoreCase("green")){
            return -1;
        }else if(this.getDangerScore().equalsIgnoreCase("yellow")){
            if(f.getDangerScore().equalsIgnoreCase("red")){
                return 1;
            }else return -1;
        }else{
            return 1;
        }
    }
}
