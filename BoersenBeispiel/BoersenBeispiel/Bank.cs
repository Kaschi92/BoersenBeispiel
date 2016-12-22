using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace BoersenBeispiel
{
    public class GlobalVar
    {
        static int id = 0;
        static Int64 accNumberInt = 1000000000;
        public static string accNumber {set; get;}

        public int getID()
        {
            return id;
        }
        public void setID(int _id)
        {
            id = _id;
        }

        public string generateIBAN()
        {
            Int64 _kontonummer = accNumberInt + 1;
            accNumberInt = _kontonummer;

            Int64 dr = (1 + (_kontonummer - 1) % 9);
            Int64 pz = 7 - dr % 7;

            pz = pz * 10000000000;
            pz = pz + _kontonummer;

            string pruefZiffer = pz.ToString();

            return pruefZiffer;
        }
    }

    public class Bank
    {
        
    }

    public class Customer
    {
        public string firstname { get; set; }
        public string lastname { get; set; }
        public string birthdate { get; set; }
        public string address { get; set; }
        public string location { get; set; }
        public string telephoneNr { get; set; }
        public Depot depot { get; set; }
        public Account account { get; set; }

        // Ein Depot pro Customer

        public Customer(string _firstname, string _lastname, string _birthdate, string _address, string _location, string _telephoneNr)
        {
            this.firstname = _firstname;
            this.lastname = _lastname;
            this.birthdate = _birthdate;
            this.address = _address;
            this.location = _location;
            this.telephoneNr = _telephoneNr;
            this.depot = createDepot();
            this.account = new Account(this, 1000);
        }

        private Depot createDepot()
        {
            // Kann ein Depot anlegen
            Depot depot = new BoersenBeispiel.Depot();
            return depot;
        }
    }

    public class Account
    {
        GlobalVar global = new GlobalVar();
        public double amount { get; set; }
        public int identifier { get; set; }
        public string accountNumber { get; set; }
        Customer customer { get; set; }

        public Account(Customer _cust, double _amount)
        {
            this.customer = _cust;
            this.identifier = createId();
            this.accountNumber = global.generateIBAN();
            this.amount = _amount;
        }

        private int createId()
        {
            int identifier = global.getID();
            global.setID(identifier + 1);

            return identifier + 1;
        }

        public void einzahlen(double betrag)
        {
            this.amount += betrag;
        }

        public void abheben(double betrag)
        {
            this.amount -= betrag;
        }
    }

    public class Aktie
    {
        public string name { get; set; }
        public Guid id { get; set; }
        public int amount { get; set; }
        public double course { get; set; }

        public Aktie(string _name, Guid _id, int _amount, double _course)
        {
            this.id = _id;
            this.name = _name;
            this.amount = _amount;
            this.course = _course;
        }
    }

    public class Depot
    {
        private List<string> availableAktienList = new List<string>();
        private List<string> userOrderList = new List<string>();
        private List<Aktie> userAktienList = new List<Aktie>();

        public Depot()
        {

        }

        public List<string> getAvailableAktienList()
        {
            return availableAktienList;
        }

        public List<string> getUserOrderList()
        {
            return userOrderList;
        }

        public List<Aktie> getUserAktienList()
        {
            return userAktienList;
        }

        public void setUserAktienList(List<Aktie> aktienL)
        {
            this.userAktienList = aktienL;
        }

        // Option = 1 - zum Anzeigen in der Console
        public void showAktien(string _url, int _option)
        {
            // REST /boerse/listCourses GET

            availableAktienList.Clear();

            HttpWebRequest req = (HttpWebRequest) WebRequest.Create(_url + "/boerse/listCourses");
            req.Method = "GET";

            string result = null;
            try
            {
                using (HttpWebResponse response = (HttpWebResponse)req.GetResponse())
                {
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    result = reader.ReadToEnd();

                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        // Json parsen
                        var objects = JArray.Parse(result);
                        foreach (JObject root in objects)
                        {
                            string name = root.Value<string>("name");
                            string aktienID = root.Value<string>("aktienID");
                            double course = root.Value<double>("course");
                            int amount = root.Value<int>("amount");

                            StringBuilder sb = new StringBuilder();
                            sb.Append("Name:" + name +";");
                            sb.Append("ID:" + aktienID + ";");
                            sb.Append("Course:" + course + ";");
                            sb.Append("Amount:" + amount);

                            if (_option == 1)
                            {
                                Console.WriteLine(sb.ToString());
                            }
                            
                            availableAktienList.Add(sb.ToString());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            } 
        }

        public void buyAktien(string _url, string _aktienID, double _limit, int _amount, string _name)
        {
            // REST /boerse/buy POST
            Int32 unixTimestamp = (Int32)(DateTime.UtcNow.Subtract(new DateTime(1970, 1, 1))).TotalSeconds;
            Guid orderID = Guid.NewGuid();

            // Json erstellen
            StringBuilder sb = new StringBuilder();
            sb.Append("{\n\t\"orderID\":\"" + orderID.ToString() + "\",\n");
            sb.Append("\t\"aktienID\":\"" + _aktienID + "\",\n");
            sb.Append("\t\"amount\":\"" + _amount + "\",\n");
            sb.Append("\t\"limit\":\"" + _limit + "\",\n");
            sb.Append("\t\"timestamp\":\"" + unixTimestamp + "\",\n");
            sb.Append("\t\"hash\":\"null\"\n}");
            string jsonString = sb.ToString();

            HttpWebRequest req = (HttpWebRequest) WebRequest.Create(_url + "/boerse/buy");
            req.Method = "POST";
            req.ContentType = "application/json";
            req.ContentLength = jsonString.Length;

            
            using (StreamWriter streamWriter = new StreamWriter(req.GetRequestStream()))
            {
                streamWriter.Write(jsonString);
                streamWriter.Flush();
                streamWriter.Close();
            }

            string result = null;
            try
            {
                using (HttpWebResponse response = (HttpWebResponse)req.GetResponse())
                {
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    result = reader.ReadToEnd();

                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        if (result == null)
                        {
                            Console.WriteLine("Mind. 15 Minuten warten - Check Order");
                        }
                        else
                        {
                            Console.WriteLine(result);
                        }

                        StringBuilder sb1 = new StringBuilder();
                        sb1.Append("Name:" + _name + ";");
                        sb1.Append("OrderID:" + orderID + ";");
                        sb1.Append("URL:" + _url + ";");
                        sb1.Append("AktienID:" + _aktienID + ";");
                        sb1.Append("BUY");

                        userOrderList.Add(sb1.ToString());
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void sellAktien(string _url, string _aktienID, double _limit, int _amount, string _name)
        {
            // REST /boerse/sell POST
            Int32 unixTimestamp = (Int32)(DateTime.UtcNow.Subtract(new DateTime(1970, 1, 1))).TotalSeconds;
            Guid orderID = Guid.NewGuid();

            // Json erstellen
            StringBuilder sb = new StringBuilder();
            sb.Append("{\n\t\"orderID\":\"" + orderID.ToString() + "\",\n");
            sb.Append("\t\"aktienID\":\"" + _aktienID + "\",\n");
            sb.Append("\t\"amount\":\"" + _amount + "\",\n");
            sb.Append("\t\"limit\":\"" + _limit + "\",\n");
            sb.Append("\t\"timestamp\":\"" + unixTimestamp + "\",\n");
            sb.Append("\t\"hash\":\"null\"\n}");
            string jsonString = sb.ToString();

            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(_url + "/boerse/sell");
            req.Method = "POST";
            req.ContentType = "application/json";
            req.ContentLength = jsonString.Length;


            using (StreamWriter streamWriter = new StreamWriter(req.GetRequestStream()))
            {
                streamWriter.Write(jsonString);
                streamWriter.Flush();
                streamWriter.Close();
            }

            string result = null;
            try
            {
                using (HttpWebResponse response = (HttpWebResponse)req.GetResponse())
                {
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    result = reader.ReadToEnd();

                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        if (result == null)
                        {
                            Console.WriteLine("Mind. 15 Minuten warten - Check Order");
                        }
                        else
                        {
                            Console.WriteLine(result);
                        }

                        StringBuilder sb1 = new StringBuilder();
                        sb1.Append("Name:" + _name + ";");
                        sb1.Append("OrderID:" + orderID + ";");
                        sb1.Append("URL:" + _url + ";");
                        sb1.Append("AktienID:" + _aktienID + ";");
                        sb1.Append("SELL");

                        userOrderList.Add(sb1.ToString());
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void orderCheck(string _url, string _orderID, string _name, string _aktienID, Account _acc)
        {
            // REST /boerse/check POST

            // Json erstellen
            StringBuilder sb = new StringBuilder();
            sb.Append("{\n\t\"orderID\":\"" + _orderID + "\",\n");
            sb.Append("\t\"hash\":\"null\"\n}");
            string jsonString = sb.ToString();

            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(_url + "/boerse/check");
            req.Method = "POST";
            req.ContentType = "application/json";
            req.ContentLength = jsonString.Length;

            using (StreamWriter streamWriter = new StreamWriter(req.GetRequestStream()))
            {
                streamWriter.Write(jsonString);
                streamWriter.Flush();
                streamWriter.Close();
            }

            string result = null;
            try
            {
                using (HttpWebResponse response = (HttpWebResponse)req.GetResponse())
                {
                    StreamReader reader = new StreamReader(response.GetResponseStream());
                    result = reader.ReadToEnd();

                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        // Json parsen
                        var root = JObject.Parse(result);
                        string orderID = root.Value<string>("orderID");
                        double price = root.Value<double>("price");
                        int amount = root.Value<int>("amount");
                        int status = root.Value<int>("status");
                        string hash = root.Value<string>("hash");

                        if (status == 0)
                        {
                            // Successfull
                            Console.WriteLine("Aktion Successfull: Richtiger Amount - ist im Limit");

                            foreach (string s in userOrderList)
                            {
                                if (s.Contains(orderID) && s.Contains("BUY"))
                                {
                                    _acc.abheben(price);
                                    userAktienList.Add(new Aktie(_name, Guid.Parse(_aktienID), amount, price));
                                }
                                else if(s.Contains(orderID) && s.Contains("SELL"))
                                {
                                    // Aus Liste entfernen
                                    _acc.einzahlen(price);

                                    foreach (Aktie a in userAktienList)
                                    {
                                        if (a.id == Guid.Parse(_aktienID))
                                        {
                                            userAktienList.Remove(a);
                                        }
                                    }
                                }
                            }
                        }
                        else if (status == 1)
                        {
                            Console.WriteLine("Später erneut versuchen - In Progress");
                        }
                        else if (status == 2)
                        {
                            Console.WriteLine("Denied - Order wird gelöscht");

                            foreach (string s in userOrderList)
                            {
                                if (s.Contains(orderID))
                                {
                                    userOrderList.Remove(s);
                                }
                            }
                        }
                        else if (status == 3)
                        {
                            Console.WriteLine("Nicht genügend Amount - Amount wird geändert");

                            foreach (string s in userOrderList)
                            {
                                if (s.Contains(orderID) && s.Contains("BUY"))
                                {
                                    _acc.abheben(price);
                                    userAktienList.Add(new Aktie(_name, Guid.Parse(_aktienID), amount, price));
                                }
                                else if (s.Contains(orderID) && s.Contains("SELL"))
                                {
                                    // Aus Liste entfernen
                                    _acc.einzahlen(price);

                                    foreach (Aktie a in userAktienList)
                                    {
                                        if (a.id == Guid.Parse(_aktienID))
                                        {
                                            userAktienList.Remove(a);
                                        }
                                    }
                                }
                            }
                        }
                        else if (status == 4)
                        {
                            Console.WriteLine("Falscher Preis - nicht im Limit - Order wird gelöscht!");

                            foreach (string s in userOrderList)
                            {
                                if (s.Contains(orderID))
                                {
                                    userOrderList.Remove(s);
                                }
                            }
                        }
                        else
                        {
                            Console.WriteLine("Unknown Error");
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void gewinnVerlustRechnung(string _url, Aktie _aktie)
        {
            string aktienID = _aktie.id.ToString();

            showAktien(_url, 0);
            
            foreach (string s in availableAktienList)
            {
                if (s.Contains(aktienID))
                {
                    string[] splittedString = s.Split(';');
                    string name = splittedString[0].Split(':')[1];
                    string id = splittedString[1].Split(':')[1];
                    string course = splittedString[2].Split(':')[1];
                    string amount = splittedString[3].Split(':')[1];

                    Console.WriteLine("------------------------------------------");
                    Console.WriteLine("Status der gekauften Aktie:");
                    Console.WriteLine("Name: " + _aktie.name + ", Course: " + _aktie.course + ", Amount: " + _aktie.amount + ", ID: " + _aktie.id);
                    Console.WriteLine("------------------------------------------");
                    Console.WriteLine("Status der Aktie der URL:");
                    Console.WriteLine("Name: " + name + ", Course: " + course + ", Amount: " + amount + ", ID: " + id);
                }
            }
        }
    }
}
