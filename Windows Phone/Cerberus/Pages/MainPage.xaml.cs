using System;
using System.Net;
using System.Windows;
using Microsoft.Phone.Controls;
using System.Windows.Navigation;
using System.IO.IsolatedStorage;

namespace Cerberus {
    public enum EmprestimoState { Devolver, PegarEmprestado }

    public partial class MainPage : PhoneApplicationPage {
        // Fields
        IsolatedStorageSettings settings;
        private static bool checkedConnection = false;
        private static string phoneName;
        public static string PhoneName { get { return phoneName; } }

        // Constructor
        public MainPage() {
            InitializeComponent();
            settings = IsolatedStorageSettings.ApplicationSettings;
        }

        private void button1_Click(object sender, RoutedEventArgs e) {
            NavigationService.Navigate(new Uri("/Pages/Emprestar.xaml", UriKind.Relative));
        }

        private void button3_Click(object sender, RoutedEventArgs e) {
            try {
                NavigationService.Navigate(new Uri("/Pages/EmprestimosTabela.xaml", UriKind.Relative));
            }
            catch (Exception exc) {
                System.Diagnostics.Debugger.Log(1, "info", "excep type: " + exc.GetType());
                System.Diagnostics.Debugger.Log(1, "info", "excep message: " + exc.Message);
            }
        }

        private void button2_Click(object sender, RoutedEventArgs e) {
            NavigationService.Navigate(new Uri("/Pages/Devolver.xaml", UriKind.Relative));
        }

        protected override void OnNavigatedTo(NavigationEventArgs e) {
            TestConnection();
            NavigationService.RemoveBackEntry();
            base.OnNavigatedTo(e);
        }
        
        private void TestConnection() {
            WebClient cli = new WebClient();
            cli.DownloadStringCompleted += OnDownloaded;
            cli.DownloadStringAsync(new Uri("http://192.168.72.17/index.html?rnd=" + DateTime.Now.Ticks, UriKind.Absolute));
        }

        private void OnDownloaded(object sender, DownloadStringCompletedEventArgs args) {
            if (args.Error != null && !checkedConnection) {
                checkedConnection = true;
                TitlePanel.Opacity = 0.3f;
                buttonPegarEmpr.Visibility = Visibility.Collapsed;
                buttonVerEmpres.Visibility = Visibility.Collapsed;
                panelConError.Visibility = Visibility.Visible;
                exceptionBlock.Text = "\"" + args.Error.Message + "\"";
                return;
            }
            checkedConnection = true;
            CheckState();
        }

        private void CheckState() {
            if (settings.Contains("Phone")) {
                buttonVerEmpres.IsEnabled = true;
                buttonPegarEmpr.IsEnabled = true;
                panelPhoneName.Visibility = Visibility.Collapsed;
                phoneName = (string)settings["Phone"];
                PhoneNameBlock.Text = phoneName;
                if ((EmprestimoState)settings["State"] == EmprestimoState.Devolver) {
                    buttonDevolver.Visibility = Visibility.Visible;
                    buttonPegarEmpr.Visibility = Visibility.Collapsed;
                }
                else if ((EmprestimoState)settings["State"] == EmprestimoState.PegarEmprestado) {
                    buttonDevolver.Visibility = Visibility.Collapsed;
                    buttonPegarEmpr.Visibility = Visibility.Visible;
                }
            }
            else {
                TitlePanel.Opacity = 0.3f;
                buttonVerEmpres.Visibility = Visibility.Collapsed;
                buttonPegarEmpr.Visibility = Visibility.Collapsed;
                panelPhoneName.Visibility = Visibility.Visible;
            }
        }

        private void buttonOkName_Click(object sender, RoutedEventArgs e) {
            settings.Add("Phone", PhoneNameBlock.Text = phoneName = textBoxName.Text);
            settings.Add("State", EmprestimoState.PegarEmprestado);
            TitlePanel.Opacity = 1.0f;
            panelPhoneName.Visibility = Visibility.Collapsed;
            buttonPegarEmpr.Visibility = Visibility.Visible;
            buttonVerEmpres.Visibility = Visibility.Visible;
            buttonVerEmpres.IsEnabled = true;
            CreatePhoneDataTable();
        }

        private void CreatePhoneDataTable() {
            WebClient cli = new WebClient();
            cli.DownloadStringAsync(new Uri(string.Format("http://192.168.72.17/post.php?psswd=mOrGoTh*&phone={0}", Uri.EscapeDataString(MainPage.PhoneName)), UriKind.Absolute));
        }
    }
}