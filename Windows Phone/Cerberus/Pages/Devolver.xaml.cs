using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using System.IO.IsolatedStorage;

namespace Cerberus
{
    public partial class Devolver : PhoneApplicationPage
    {
        private WebClient cli;

        public Devolver()
        {
            InitializeComponent();

            cli = new WebClient();
            cli.DownloadStringCompleted += OnDowloaded;
            DateTime now = DateTime.Now;

            nomeBox.Text = "...";
            dataBox.Text = now.Day + "-" + ToString(now.Month) + "-" + (now.Year - 2000);
            horaBox.Text = string.Format("{0}:{1,2:00}:{2,2:00} {3}", ((now.Hour + 11) % 12 + 1), now.Minute, now.Second, (now.Hour >= 12) ? "PM" : "AM");

            WebClient cli2 = new WebClient();
            cli2.DownloadStringCompleted += OnCli2Dowloaded;
            cli2.DownloadStringAsync(new Uri(string.Format("http://192.168.72.17/get.php?phone={0}&nome={1}", Uri.EscapeDataString(MainPage.PhoneName), Uri.EscapeDataString(DateTime.Now.Ticks.ToString()))), UriKind.Absolute);
        }

        private void OnCli2Dowloaded(object sender, DownloadStringCompletedEventArgs args) {
            nomeBox.Text = args.Result;
        }

        private void OKButton_Click(object sender, RoutedEventArgs e)
        {
            cli.DownloadStringAsync(new Uri(string.Format("http://192.168.72.17/post.php?psswd=mOrGoTh*&phone={0}&devolucao={1}", Uri.EscapeDataString(MainPage.PhoneName), Uri.EscapeDataString(dataBox.Text + " " + horaBox.Text)), UriKind.Absolute));
            OKButton.Content = "Sending...";
            IsolatedStorageSettings.ApplicationSettings["State"] = EmprestimoState.PegarEmprestado;
        }

        private void OnDowloaded(object sender, DownloadStringCompletedEventArgs args) {
            NavigationService.Navigate(new Uri("/Pages/EmprestimosTabela.xaml", UriKind.Relative));
        }

        private string ToString(int month) {
            switch (month) {
                case 1: return "Jan";
                case 2: return "Feb";
                case 3: return "Mar";
                case 4: return "Apr";
                case 5: return "May";
                case 6: return "Jun";
                case 7: return "Jul";
                case 8: return "Aug";
                case 9: return "Sep";
                case 10: return "Oct";
                case 11: return "Nov";
                case 12: return "Dec";
                default: return "---";
            }
        }
    }
}