import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.iot.model.v20170420.PubRequest;
import com.aliyuncs.iot.model.v20170420.PubResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.net.util.Base64;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * The application which can send order to devices. It depends on Aliyun-IoT-SDK
 *
 * @version 1.00 2018-05-04
 * @author Letao Shen(沈乐涛)
 */
public class Form {
    private JButton button1;
    private JPanel panel1;
    private JTextArea textChat;
    private JTextField textSend;
    private JTextField textAccessSecret;
    private JTextField textProductKey;
    private JTextField textDeviceName;
    private JTextField textAccessKey;

    public Form() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String accessKey = textAccessKey.getText();
                    String accessSecret = textAccessSecret.getText();
                    DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Iot", "iot.cn-shanghai.aliyuncs.com");
                    IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKey, accessSecret);
                    DefaultAcsClient client = new DefaultAcsClient(profile); //初始化SDK客户端

                    PubRequest request = new PubRequest();
                    request.setProductKey(textProductKey.getText());
                    request.setMessageContent(Base64.encodeBase64String(textSend.getText().getBytes()));
                    request.setTopicFullName("/"+textProductKey.getText()+"/"+textDeviceName.getText()+"/get");
                    request.setQos(0); //目前支持QoS0和QoS1
                    PubResponse response = client.getAcsResponse(request);
                    if (response.getSuccess()) {
                        JOptionPane.showMessageDialog(panel1, "发送成功！", "发送结果", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(panel1, "发送失败：\n"+response.getErrorMessage(), "发送结果", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(panel1, "请正确填写内容", "内容错误", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("下发命令到设备");
        frame.setContentPane(new Form().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        System.out.println("started");
    }
}
