//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace FunitureManager.Models
{
    using Newtonsoft.Json;
    using System;
    using System.Collections.Generic;
    using System.Runtime.Serialization;

    public partial class Order_Detail
    {
        public System.Guid Id_Order { get; set; }
        public System.Guid Id_Product { get; set; }
        public int Quantity { get; set; }
        public decimal Price { get; set; }
        public decimal Total_Price { get; set; }
        public Nullable<bool> Status { get; set; }
        [JsonIgnore]
        [IgnoreDataMember]
        public virtual Order Order { get; set; }
        [JsonIgnore]
        [IgnoreDataMember]
        public virtual Product Product { get; set; }
    }
}
