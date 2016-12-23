using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Reflection;

namespace BoersenBeispiel
{
    class Program
    {
        static void init(Customer _externCust1, Customer _internCust1, Customer _internCust2, Customer _internCust3, string _externURL, string _internURL)
        {
            // Initialisiert die aktienIDs für die Verkäufer

            // Für 1 Externen Customer Aktionen
            List<Aktie> externCust1List = _externCust1.depot.getUserAktienList();

            _externCust1.depot.showAktien(_externURL, 0);
            List<string> availableAktienList = _externCust1.depot.getAvailableAktienList();
            string[] splittedString = availableAktienList[0].Split(';');
            string name = splittedString[0].Split(':')[1];
            string id = splittedString[1].Split(':')[1];
            externCust1List.Add(new Aktie(name, Guid.Parse(id), 1, 180));
            _externCust1.depot.setUserAktienList(externCust1List);

            // Für die 3 Internen Customer Aktionen
            List<Aktie> internCust1List = _internCust1.depot.getUserAktienList();
            List<Aktie> internCust2List = _internCust2.depot.getUserAktienList();
            List<Aktie> internCust3List = _internCust3.depot.getUserAktienList();

            _internCust1.depot.showAktien(_internURL, 0);
            availableAktienList = _internCust1.depot.getAvailableAktienList();
            splittedString = availableAktienList[0].Split(';');
            name = splittedString[0].Split(':')[1];
            id = splittedString[1].Split(':')[1];

            internCust1List.Add(new Aktie(name, Guid.Parse(id), 1, 300));
            internCust2List.Add(new Aktie(name, Guid.Parse(id), 1, 290));
            internCust3List.Add(new Aktie(name, Guid.Parse(id), 1, 310));

            _internCust1.depot.setUserAktienList(internCust1List);
            _internCust2.depot.setUserAktienList(internCust2List);
            _internCust3.depot.setUserAktienList(internCust3List);
        }

        static void Main(string[] args)
        {
            string externURL1 = "https://boerse.pwnhofer.at";
            string internURL = "http://ec2-35-165-217-150.us-west-2.compute.amazonaws.com:4000";

            // USER externe Aktionen
            Customer cust1 = new Customer("Stephanie", "Kaschnitz", "05.07.1992", "Testaddresse 1", "Salzburg", "06641234567");
            Customer cust2 = new Customer("Christopher", "Wieland", "10.10.1993", "Testaddresse 2", "Salzburg", "06641278567");

            // USER interne Aktionen
            // Verkauf
            Customer cust3 = new Customer("Martin", "Wieser", "07.12.1991", "Testaddresse 3", "Salzburg", "06641279567");
            Customer cust4 = new Customer("John", "Doe", "21.11.1994", "Testaddresse 4", "Salzburg", "06641273453");
            Customer cust5 = new Customer("Max", "Mustermann", "13.07.1990", "Testaddresse 5", "Salzburg", "06641662567");

            // Kauf
            Customer cust6 = new Customer("Julia", "Musterfrau", "30.05.1992", "Testaddresse 6", "Salzburg", "06641789567");
            Customer cust7 = new Customer("Julian", "Lipp", "22.01.1993", "Testaddresse 7", "Salzburg", "06641212347");
            Customer cust8 = new Customer("Herbert", "Trinkdas", "01.02.1993", "Testaddresse 8", "Salzburg", "06641276666");

            // Initialisieren
            init(cust1, cust3, cust4, cust5, externURL1, internURL);

            List<Aktie> aktienList1 = cust1.depot.getUserAktienList();

            // Gewinn Verlust
            Aktie aktie = aktienList1[0];
            cust1.depot.gewinnVerlustRechnung(externURL1, aktie);

            // User verkauft extern
            verkauf(cust1, externURL1);

            // User kauft extern
            kauf(cust2, externURL1);

            // 3 User verkaufen intern
            verkauf(cust3, internURL);
            verkauf(cust4, internURL);
            verkauf(cust5, internURL);

            // 3 User kaufen intern
            kauf(cust6, internURL);
            kauf(cust7, internURL);
            kauf(cust8, internURL);

            Console.ReadKey();
        }

        static void verkauf(Customer _cust, string _url)
        {
            List<Aktie> aktienList = _cust.depot.getUserAktienList();
            _cust.depot.sellAktien(_url, aktienList[0].id.ToString(), 400, 1, aktienList[0].name);

            // OrderCheck
            string orderID = null;
            List<string> orderList = _cust.depot.getUserOrderList();
            foreach (string s in orderList)
            {
                if (s.Contains(aktienList[0].id.ToString()))
                {
                    string[] splittedOrderString = s.Split(';');
                    orderID = splittedOrderString[1].Split(':')[1];
                }
            }
            _cust.depot.orderCheck(_url, orderID, aktienList[0].name, aktienList[0].id.ToString(), _cust.account);
        }

        static void kauf(Customer _cust, string _url)
        {
            _cust.depot.showAktien(_url, 1);

            // Erste Aktie auswählen und kaufen
            List<string> availableAktienList = _cust.depot.getAvailableAktienList();
            string[] splittedString = availableAktienList[0].Split(';');
            string name = splittedString[0].Split(':')[1];
            string id = splittedString[1].Split(':')[1];

            _cust.depot.buyAktien(_url, id, 300, 1, name);

            // OrderCheck
            string orderID = null;
            List<string> orderList = _cust.depot.getUserOrderList();
            foreach (string s in orderList)
            {
                if (s.Contains(id))
                {
                    string[] splittedOrderString = s.Split(';');
                    orderID = splittedOrderString[1].Split(':')[1];
                    name = splittedOrderString[0].Split(':')[1];
                }
            }
            _cust.depot.orderCheck(_url, orderID, name, id, _cust.account);
        }
    }
}
