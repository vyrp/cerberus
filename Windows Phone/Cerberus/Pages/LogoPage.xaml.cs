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
using System.Threading;
using System.Windows.Threading;

namespace Cerberus.Pages {
    public partial class LogoPage : PhoneApplicationPage {
        // Fields
        private DispatcherTimer t = new DispatcherTimer();

        // Constructor
        public LogoPage() {
            InitializeComponent();
        }

        // Methods
        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e) {
            t.Interval = new TimeSpan(0, 0, 0, 1, 0); // 1s
            t.Tick += OnTick;
            t.Start();
            base.OnNavigatedTo(e);
        }

        private void OnTick(object sender, EventArgs args) {
            t.Stop();
            NavigationService.Navigate(new Uri("/Pages/MainPage.xaml", UriKind.Relative));
        }
    }
}