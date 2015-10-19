package com.nv;

import org.apache.log4j.Logger;

import com.nv.pcap.IPPacket;
import com.nv.pcap.Packet;
import com.nv.pcap.PcapParser;
import com.nv.pcap.TCPPacket;
import com.nv.pcap.UDPPacket;

public class Main {
	private static Logger log = Logger.getLogger(Main.class);
	public static void main(String[] args) {  
        final String FILE_NAME = "pack1.pcap"; 
        PcapParser pcapParser = new PcapParser();
        if(pcapParser.openFile(FILE_NAME) < 0){
            System.err.println("Failed to open " + ", exiting.");
            return;
        }

        Packet packet = pcapParser.getPacket();
        while(packet != Packet.EOF){
            if(!(packet instanceof IPPacket)){
                packet = pcapParser.getPacket();
                continue;
            }

            System.out.println("--PACKET--");
            IPPacket ipPacket = (IPPacket) packet;
	    System.out.println("TIME " + ipPacket.timestamp / 1000);
            System.out.println("SRC " + ipPacket.src_ip.getHostAddress());
            System.out.println("DST " + ipPacket.dst_ip.getHostAddress());

            if(ipPacket instanceof UDPPacket){
                UDPPacket udpPacket = (UDPPacket) ipPacket;
                System.out.println("SRC PORT " + udpPacket.src_port);
                System.out.println("DST PORT " + udpPacket.dst_port);
                System.out.println("PAYLOAD LEN " + udpPacket.data.length);
            }
            if(ipPacket instanceof TCPPacket){
                TCPPacket tcpPacket = (TCPPacket) ipPacket;
                System.out.println("SRC PORT " + tcpPacket.src_port);
                System.out.println("DST PORT " + tcpPacket.dst_port);
                System.out.println("PAYLOAD LEN " + tcpPacket.data.length);
            }

            packet = pcapParser.getPacket();
        }
        pcapParser.closeFile();
//        StringBuilder errbuf = new StringBuilder(); // For any error msgs  
//  
//        /*************************************************************************** 
//         * First - we open up the selected device 
//         **************************************************************************/  
//        Pcap pcap = Pcap.openOffline(FILE_NAME, errbuf);  
//  
//        if (pcap == null) {  
//            System.err.printf("Error while opening file for capture: "  
//                + errbuf.toString());  
//            return;  
//        }  
//  
//        /*************************************************************************** 
//         * Second - we create our main loop and our application We create some 
//         * objects we will be using and reusing inside the loop 
//         **************************************************************************/  
//        Tcp tcp = new Tcp();
//        Ip4 ip = new Ip4();  
//        Ethernet eth = new Ethernet();  
//        PcapHeader hdr = new PcapHeader(JMemory.POINTER);  
//        JBuffer buf = new JBuffer(JMemory.POINTER);  
//  
//        /*************************************************************************** 
//         * Third - we must map pcap's data-link-type to jNetPcap's protocol IDs. 
//         * This is needed by the scanner so that it knows what the first header in 
//         * the packet is. 
//         **************************************************************************/  
//        int id = JRegistry.mapDLTToId(pcap.datalink());  
//  
//        /*************************************************************************** 
//         * Fourth - we peer header and buffer (not copy, think of C pointers) 
//         **************************************************************************/  
//        while (pcap.nextEx(hdr, buf) == Pcap.NEXT_EX_OK) {  
//  
//            /************************************************************************* 
//             * Fifth - we copy header and buffer data to new packet object 
//             ************************************************************************/  
//            PcapPacket packet = new PcapPacket(hdr, buf);  
//  
//            /************************************************************************* 
//             * Six- we scan the new packet to discover what headers it contains 
//             ************************************************************************/  
//            packet.scan(id);  
//  
//            /* 
//             * We use FormatUtils (found in org.jnetpcap.packet.format package), to 
//             * convert our raw addresses to a human readable string. 
//             */  
//			if (packet.hasHeader(tcp)) {
//
//				if (packet.hasHeader(eth)) {
//					String str = FormatUtils.mac(eth.source());
//					System.out.printf("#%d: eth.src=%s\n",
//							packet.getFrameNumber(), str);
//				}
//				if (packet.hasHeader(ip)) {
//					String str = FormatUtils.ip(ip.source());
//					System.out.printf("#%d: ip.src=%s\n",
//							packet.getFrameNumber(), str);
//				}
//			}
//        }  
//  
//        /************************************************************************* 
//         * Last thing to do is close the pcap handle 
//         ************************************************************************/  
//        pcap.close();  
    }  

}
