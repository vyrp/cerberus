using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace Cerberus
{
    public class Entry
    {
        //fields
        public string emprestimo;
        public string nome;
        public string devolucao;

        //ctor
        public Entry(string emprestimo, string nome, string devolucao)
        {
            this.emprestimo = emprestimo;
            this.nome = nome;
            this.devolucao = devolucao;
        }
    }
}
