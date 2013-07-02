using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using Microsoft.Phone.Controls;
using System.ComponentModel;
using System.Windows.Navigation;

namespace Cerberus
{
    public partial class EmprestimosTabela : PhoneApplicationPage
    {
        private WebClient client;
        private List<Entry> entradas;

        public EmprestimosTabela()
        {
            InitializeComponent();
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            client = new WebClient();
            client.DownloadStringCompleted += OnDownloaded;
            client.DownloadStringAsync(new Uri(string.Format("http://192.168.72.17/get.php?phone={0}&rnd={1}", MainPage.PhoneName, DateTime.Now.Ticks)), UriKind.Absolute);
            base.OnNavigatedTo(e);
        }

        protected override void OnNavigatingFrom(NavigatingCancelEventArgs e) {
            client = null;
            base.OnNavigatingFrom(e);
        }

        protected override void OnBackKeyPress(CancelEventArgs e)
        {
            NavigationService.RemoveBackEntry();
            NavigationService.RemoveBackEntry();
            NavigationService.Navigate(new Uri("/Pages/MainPage.xaml", UriKind.Relative));
        }

        private void OnDownloaded(object sender, DownloadStringCompletedEventArgs args)
        {
            entradas = parse(args.Result);

            for (int j = 1, i = entradas.Count - 5; i < entradas.Count; i++)
            {
                if (i < 0)
                    continue;

                TextBlock emprestimoBlock = new TextBlock();
                TextBlock nameBlock = new TextBlock();
                TextBlock devolucaoBlock = new TextBlock();

                emprestimoBlock.SetValue(Grid.ColumnProperty, 0);
                nameBlock.SetValue(Grid.ColumnProperty, 1);
                devolucaoBlock.SetValue(Grid.ColumnProperty, 2);

                emprestimoBlock.SetValue(Grid.RowProperty, j);
                nameBlock.SetValue(Grid.RowProperty, j);
                devolucaoBlock.SetValue(Grid.RowProperty, j);

                emprestimoBlock.HorizontalAlignment = HorizontalAlignment.Center;
                nameBlock.HorizontalAlignment = HorizontalAlignment.Center;
                devolucaoBlock.HorizontalAlignment = HorizontalAlignment.Center;

                emprestimoBlock.VerticalAlignment = VerticalAlignment.Center;
                nameBlock.VerticalAlignment = VerticalAlignment.Center;
                devolucaoBlock.VerticalAlignment = VerticalAlignment.Center;

                emprestimoBlock.Text = entradas[i].emprestimo;
                nameBlock.Text = entradas[i].nome;
                devolucaoBlock.Text = entradas[i].devolucao;

                nameBlock.TextAlignment = TextAlignment.Center;
                nameBlock.FontStyle = FontStyles.Italic;

                ContentPanel.Children.Add(emprestimoBlock);
                ContentPanel.Children.Add(nameBlock);
                ContentPanel.Children.Add(devolucaoBlock);

                j++;
            }

        }

        private List<Entry> parse(string data)
        {
            List<Entry> entries = new List<Entry>();
            string[] rows = data.Split(new string[] { "||" }, StringSplitOptions.RemoveEmptyEntries);
            foreach (string row in rows)
            {
                string[] items = row.Split(';');
                entries.Add(new Entry(DateParse(items[0]), items[1], (("".Equals(items[2]))?" - ":DateParse(items[2]))));
            }
            return entries;
        }

        private string DateParse(string date)
        {
            string[] datetime = date.Split(' ');
            return datetime[0] + "\n" + datetime[1] + " " + datetime[2];
        }
    }
}