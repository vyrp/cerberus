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
using System.Windows.Navigation;
using System.IO.IsolatedStorage;

namespace Cerberus {
    public partial class Emprestar : PhoneApplicationPage {
        private WebClient cli;

        public Emprestar() {
            InitializeComponent();

            DateTime now = DateTime.Now;
            dataBox.Text = now.Day + "-" + ToString(now.Month) + "-" + (now.Year-2000);
            horaBox.Text = string.Format("{0}:{1,2:00}:{2,2:00} {3}", ((now.Hour + 11) % 12 + 1), now.Minute, now.Second, (now.Hour >= 12)?"PM":"AM");

            cli = new WebClient();
            cli.DownloadStringCompleted += OnDowloaded;
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

        protected override void OnNavigatedTo(NavigationEventArgs e) {
            base.OnNavigatedTo(e);
        }

        private void button1_Click(object sender, RoutedEventArgs e) {
            cli.DownloadStringAsync(new Uri(string.Format("http://192.168.72.17/post.php?psswd=mOrGoTh*&emprestimo={0}&nome={1}&phone={2}", Uri.EscapeDataString(dataBox.Text + " " + horaBox.Text), Uri.EscapeDataString(nomeBox.Text), Uri.EscapeDataString(MainPage.PhoneName)), UriKind.Absolute));
            button1.Content = "Sending...";
            IsolatedStorageSettings.ApplicationSettings["State"] = EmprestimoState.Devolver;
        }

        private void OnDowloaded(object sender, DownloadStringCompletedEventArgs args) {
            NavigationService.Navigate(new Uri("/Pages/EmprestimosTabela.xaml", UriKind.Relative));
        }
    }
}