package org.praveen.practice;

public class Application {
    public static void main(String[] args) throws Exception{

        TransactionHistory sangeethaHistory = new TransactionHistory(15331,"Sangeetha","Credit",10000);
        TransactionHistory nehaHistory = new TransactionHistory(14531,"Neha","Credit",7000);
        TransactionHistory mohitHistory = new TransactionHistory(19031,"Mohit","Debit",2000);
        TransactionHistory joshHistory = new TransactionHistory(25389,"Josh","Debit",9000);
        Hibernate<TransactionHistory> hibernate = Hibernate.getConnection();
        hibernate.write(sangeethaHistory);
        hibernate.write(nehaHistory);
        hibernate.write(mohitHistory);
        hibernate.write(joshHistory);

        TransactionHistory obj = hibernate.read(TransactionHistory.class, 1L);
        System.out.println(obj);

    }
}
