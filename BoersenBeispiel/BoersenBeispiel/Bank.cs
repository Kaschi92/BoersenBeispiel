using System;
using System.Collections.Generic;
using System.Linq;
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

        // Ein Depot pro Customer

        public Customer(string _firstname, string _lastname, string _birthdate, string _address, string _location, string _telephoneNr)
        {
            this.firstname = _firstname;
            this.lastname = _lastname;
            this.birthdate = _birthdate;
            this.address = _address;
            this.location = _location;
            this.telephoneNr = _telephoneNr;
        }

        public void createDepot()
        {
            // Kann ein Depot anlegen
        }
    }

    public class Depot
    {
        // Beinhaltet Aktien und Kurse, Gewinn, Verlustrechnung
        // Orderbuch - kauf/verkauf

        public Depot()
        {
            // ToDo
        }

        public void showAktien()
        {

        }

        public void showKurse()
        {

        }

        public void gewinnVerlustRechnung()
        {

        }

    }

    public class Account
    {
        GlobalVar global = new GlobalVar();
        public double amount { get; set; }
        public int identifier { get; set; }
        public string accountNumber { get; set; }
        Customer customer { get; set; }
        //GlobalVar global { get; set; }
        

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
    }
}
