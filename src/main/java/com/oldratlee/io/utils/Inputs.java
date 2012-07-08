package com.oldratlee.io.utils;

import java.io.*;

import com.oldratlee.io.core.Input;
import com.oldratlee.io.core.Output;
import com.oldratlee.io.core.Receiver;
import com.oldratlee.io.core.Sender;

/**
 * Utils of {@link Input}.
 * 
 * @author oldratlee
 */
public class Inputs {
    
    static class TextInput implements Input<String, IOException> {
        final File source;
        final Reader reader;
        final TextSender sender; 
        
        public TextInput(File source) throws IOException {
            this.source = source;
            reader = new FileReader(source);

            sender = new TextSender(reader);
        }
        
        public <ReceiverThrowableType extends Throwable> void transferTo(Output<String, ReceiverThrowableType> output)
                throws IOException, ReceiverThrowableType {
            output.receiveFrom(sender);
            
            try {
                reader.close();
            } catch (Exception e) {
                // ignore finished exception :)
            }
        }
        
    }
    
    static class TextSender implements Sender<String, IOException> {
        final Reader reader;
	    BufferedReader buufferReader;

        public TextSender(Reader reader) throws FileNotFoundException {
            this.reader = reader;
	        this.buufferReader = new BufferedReader(reader);
        }

        public <ReceiverThrowableType extends Throwable> void sendTo(Receiver<String, ReceiverThrowableType> receiver)
                throws ReceiverThrowableType, IOException {
            String readLine;
            while((readLine = buufferReader.readLine()) != null) {
                receiver.receive(readLine + "\n");
            }
        }
    }
    
    public static Input<String, IOException> text(File source) throws IOException {
        return new TextInput(source);
    }
}
