using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Ozinbo.Model.Dto
{
    public class LoginModel
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public List<BankModel> Banks { get; set; }
        public List<TelcoModel> Telcos { get; set; }
        public List<BillerModel> Billers { get; set; }
    }

}
