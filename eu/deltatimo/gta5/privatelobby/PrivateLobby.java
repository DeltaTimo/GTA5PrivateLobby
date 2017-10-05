package deltatimo.gta5.privatelobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class PrivateLobby {
  public static void main(String[] args) {

    final String ports = "6672,61455,61457,61458";

    Scanner in = new Scanner(System.in);
    System.out.println("         EN              //            DE");
    System.out.println("Enable private lobbies?  // Private Lobby aktivieren?");
    String strEnable = in.nextLine().trim().toLowerCase();
    System.out.println(strEnable);
    if (strEnable.matches("y") || strEnable.matches("yes") || strEnable.matches("j")
        || strEnable.matches("ja")) {
    } else if (strEnable.matches("n") || strEnable.matches("no") || strEnable.matches("nein")) {
      System.out.println("Disabling private lobby! // Private Lobby wird deaktiviert!");
      try {
        String runStr =
            "netsh advfirewall firewall set rule name=\"GTA5PrivateLobby\" new enable=no";
        Process proc = Runtime.getRuntime().exec(runStr);
        proc.waitFor();
        System.out.println("Private Lobby disabled!  // Private Lobby deaktiviert!");
      } catch (Exception e) {
        System.out.println("Something went wrong!");
      }
      System.exit(0);
    } else {
      System.exit(0);
    }

    System.out.println("Please enter the IPs of  // Bitte die IP-Adressen deiner");
    System.out.println("your exceptions          // Ausnahmen eingeben ");
    System.out.println("(Confirm each address    // (nach jeder Adresse mit");
    System.out.println("  by pressing enter)     //    Enter bestaetigen)");
    System.out.println("Press enter to reenable  // Druecke Enter um vorher eingestellte");
    System.out.println("with previously used IPs // IP-Adressen wieder zu aktivieren");
    System.out.println("To block all IPs enter   // Um alle Adressen zu blockieren,");
    System.out.println("       'none'            //         gib 'keine' ein");

    boolean blockAllIPs = false;
    ArrayList<IPv4> ips = new ArrayList<IPv4>();
    String line;
    while (true) {
      line = in.nextLine();
      if (line.trim().matches("")) {
        break;
      }
      if (line.trim().toLowerCase().matches("none") || line.trim().toLowerCase().matches("keine")) {
        System.out.println("You have removed all IPs // Alle IP Adressen geloescht");
        blockAllIPs = true;
        break;
      }
      IPv4 ip = IPv4.getFromString(line);
      if (!ip.isValid()) {
        System.out.println("Fehler! Press enter to   // Fehler! Druecke");
        System.out.println("apply rules to firewall  // Enter um fortzufahren oder");
        System.out.println("or enter more addresses  // gib weitere Adressen ein");
        continue;
      }
      System.out.println("Added! Press enter to    // Erfolgreich hinzugefuegt! Druecke");
      System.out.println("apply rules to firewall  // Enter um fortzufahren oder");
      System.out.println("or enter more addresses  // gib weitere Adressen ein");
      if (!ip.equals(IPv4.getFromString("0.0.0.0"))
          && !ip.equals(IPv4.getFromString("255.255.255.255")) && !ips.contains(ip)) {
        ips.add(ip);
      }
    }
    in.close();
    if (ips.size() <= 0 && !blockAllIPs) {
      System.out.println("You have added no IPs.");
      // TODO: Remove all IPs from List.
      try {
        String runStr =
            "netsh advfirewall firewall set rule name=\"GTA5PrivateLobby\" new enable=yes";
        // System.out.println(runStr);
        Process proc = Runtime.getRuntime().exec(runStr);
        proc.waitFor();
        System.out.println("Private Lobby enabled!   // Private Lobby aktiviert!");
      } catch (Exception e) {
        System.out.println("Something went wrong!");
      }
    } else {
      Collections.sort(ips);

      String resultStr = "0.0.0.0";
      IPv4 precedingIP;
      for (IPv4 ip : ips) {
        if (ips.contains(ip.getFollowing())) {
          if (ips.contains(ip.getPreceding()))
            continue;
          resultStr += "-" + ip.getPreceding() + ",";
        } else if (ips.contains(ip.getPreceding())) {
          resultStr += ip.getFollowing();
        } else if (ips.contains(ip.getPreceding().getPreceding()))
          resultStr += "," + ip.getFollowing() + "";
        else
          resultStr += "-" + ip.getPreceding() + "," + ip.getFollowing() + "";
      }
      resultStr += "-255.255.255.255";

      String runStr;
      Process proc;
      try {
        runStr = "netsh advfirewall firewall delete rule name=\"GTA5PrivateLobby\"";
        proc = Runtime.getRuntime().exec(runStr);
        proc.waitFor();
        runStr =
            "netsh advfirewall firewall add rule name=\"GTA5PrivateLobby\" dir=out action=block enable=yes protocol=udp description=\"Blocks GTA5 Lobby Ports to remove unwanted guests from 'public' lobbies.\" localport="
                + ports + " remoteip=" + resultStr;
        proc = Runtime.getRuntime().exec(runStr);
        proc.waitFor();
        runStr =
            "netsh advfirewall firewall add rule name=\"GTA5PrivateLobby\" dir=in action=block enable=yes protocol=udp description=\"Blocks GTA5 Lobby Ports to remove unwanted guests from 'public' lobbies.\" localport="
                + ports + " remoteip=" + resultStr;
        // System.out.println(runStr);
        proc = Runtime.getRuntime().exec(runStr);
        proc.waitFor();
        System.out.println("Private Lobby enabled    // Private Lobby aktiviert");
        System.out.println(" with new ip-adresses    // mit neuen IP-Addressen");
      } catch (Exception e) {
        System.out.println("Something went wrong!");
      }
    }
  }
}
