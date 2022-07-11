package savingData;

import BankSystem.BankSystem;

import java.io.*;

public class dataSaving implements Serializable {
    private BankSystem engine;

    public dataSaving() {}

    public BankSystem getMainEngine() {
        return engine;
    }

    public void setData(BankSystem i_engine){
        engine = i_engine;
    }

    public void savingData() {
        String FileName = "savedDataFile.bat";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FileName))) {
            out.writeObject(engine);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BankSystem loadingData(){
        String FileName = "savedDataFile.bat";
        try (ObjectInputStream in = (new ObjectInputStream(new FileInputStream(FileName)))) {
            engine = (BankSystem) in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return engine;
    }
}

