function calc(start,end,pricePerDayFlag){
	if(pricePerDayFlag)
	{
		var pricePerUnit = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit')")
		var avdd = eval("document.getElementById('competitor."+start+"."+end+".avdd')")
		var priceperday = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit#displayFieldText')")
		var priceperdayVal = eval("document.getElementById('competitor."+start+"."+end+".price_per_day')")
	if(!isNaN(pricePerUnit.value))
		{
			priceperday.innerHTML = new NumberFormat(round((pricePerUnit.value)  * (avdd.value))).toFormatted();
			priceperdayVal.value = round((pricePerUnit.value)  * (avdd.value));
		}
	}
	else
	{
		var pricePerUnit = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit')")
		var avdd = eval("document.getElementById('competitor."+start+"."+end+".avdd')")
		var courseOfTherapy = eval("document.getElementById('competitor."+start+"."+end+".course_of_therapy')")

		var priceOfTherapy = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit#displayFieldText')")
		var priceOfTherapyVal = eval("document.getElementById('competitor."+start+"."+end+".price_of_therapy')")
	if(!isNaN(pricePerUnit.value))
		{
			priceOfTherapy.innerHTML = new NumberFormat(round((courseOfTherapy.value)* (pricePerUnit.value)  * (avdd.value))).toFormatted();
			priceOfTherapyVal.value = round((courseOfTherapy.value)* (pricePerUnit.value)  * (avdd.value));
		}
	}
} 

function compute(start,end,pricePerDayFlag){
	if(pricePerDayFlag)
	{
		var pricePerUnit = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit')")
		var avdd = eval("document.getElementById('competitor."+start+"."+end+".avdd')")
		var priceperday = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit#displayFieldText')")
		var priceperdayVal = eval("document.getElementById('competitor."+start+"."+end+".price_per_day')")
	if(!isNaN(pricePerUnit.value))
		{
			priceperday.innerHTML = new NumberFormat(round((pricePerUnit.value)  * (avdd.value))).toFormatted();
			priceperdayVal.value = round((pricePerUnit.value)  * (avdd.value));
		}
	}
	else
	{
		var pricePerUnit = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit')")
		var avdd = eval("document.getElementById('competitor."+start+"."+end+".avdd')")
		var courseOfTherapy = eval("document.getElementById('competitor."+start+"."+end+".course_of_therapy')")

		var priceOfTherapy = eval("document.getElementById('competitor."+start+"."+end+".price_per_unit#displayFieldText')")
		var priceOfTherapyVal = eval("document.getElementById('competitor."+start+"."+end+".price_of_therapy')")
	if(!isNaN(pricePerUnit.value))
		{
			priceOfTherapy.innerHTML = new NumberFormat(round((courseOfTherapy.value)* (pricePerUnit.value)  * (avdd.value))).toFormatted();
			priceOfTherapyVal.value = round((courseOfTherapy.value)* (pricePerUnit.value)  * (avdd.value));
		}
	}
} 

function trim(strText)
{
	if (strText.length > 0)
	{
		while (strText.indexOf(" ")==0)
		{
			strText = strText.replace(" ","")
		}

		while (strText.lastIndexOf(" ")==strText.length-1 && strText.length > 0)
		{
			strText = strText.substring(0,(strText.length-1))
		}
	}
	return strText;
}


function ltrim(strText)
{
	while (strText.indexOf(" ")==0)
	{
	strText=strText.replace(" ","")
	}
	return strText;
}




//function to validate an Email
function chkEmail(txtElement,fieldName,allowEmpty)
{
	var exclude=/[^@\-\.\w\_]|^[_@\.\-]|[\._\-]{2}|[@\.]{2}|(@)[^@]*\1/;
	var check=/@[\w\-]+\./;
	var checkend=/\.[a-zA-Z]{2,3}$/;
	var strEmail = txtElement.value
	var email_array=strEmail.split(",");

	if(allowEmpty == false && txtElement.value.length == 0)
	{
		alert("'Please enter '" + fieldName + "'");
		txtElement.focus()
		return false;
	}

	if(allowEmpty == true && txtElement.value.length == 0)
	{
		//empty value is allowed
		return true;
	}
	else
	{
		var email_num=0;
		var checkEmail;
		while (email_num < email_array.length)
		{
			var trimemail = trim(email_array[email_num]);
			if(((trimemail.search(exclude) != -1) ||
			(trimemail.search(check)) == -1)   ||
			(trimemail.search(checkend) == -1))
			{
				checkEmail = "false";
			}
			else
			{
				checkEmail = "true";
			}
			email_num++;

			if(checkEmail == "false")
			{
				alert("'Incorrect email address'");
				txtElement.focus()
				return false;
			}
		}
	}
	return true;
}

function round(number,X) {
// rounds number to X decimal places, defaults to 2
    X = (!X ? 2 : X);
    return Math.round(number*Math.pow(10,X))/Math.pow(10,X);
}

function roundAndCent(number) {
    // rounds number with two decimal places (padding with 0)
    var amount = round(number, 2) + '';
    if(amount.indexOf('.') < 0) {
        amount = amount + ".00";
    }
    else {
        while(true)
        {
            if(amount.length - (amount.indexOf('.')+1) < 2) 
            {			
                amount+='0';
            }
            else
                break;
        }
    }
    return amount;
    
}

function cent(amount) {
// returns the amount in the .99 format
    amount -= 0;
    amount = (Math.round(amount*1000))/1000;

    //return (amount == Math.floor(amount)) ? amount + '.000' : (  (amount*100 == Math.floor(amount*100)) ? amount + '0' : amount);*/



    if(amount == Math.floor(amount))
	{
		amount += '.000'

	}

	else if(amount*10 == Math.floor(amount*10))
	{
		amount += '00'

	}

	else if(amount*100== Math.floor(amount*100))
	{
		amount += '0'

	}
	else
		amount+='';


	//loop added to pad zeros to the decimal value upto 6 places
	//on Feb0204 by laxmi
	
	while(true)

	{
		if(amount.length - (amount.indexOf('.')+1) < 6) 
		{			
			amount+='0';
		}
		else
			break;
	}


	return amount;
}



//function to validate a Numeric Field
function chkNumeric(txtElement,fieldName,minValue,maxValue,allowEmpty)
{

	minValue=0;

	if(allowEmpty==false && txtElement.value.length == 0){
		alert("Please enter " + fieldName);
		txtElement.focus();
		return false;
	}
	if(isNaN(txtElement.value))	{
		alert("Please enter a Numeric " + fieldName + "");
		txtElement.focus();
		return false;
	}
	if(parseFloat(txtElement.value) < minValue){
		alert(fieldName + " should not be less than " + minValue);
		txtElement.focus();
		return false;
	}
	if(maxValue != 0 && parseFloat(txtElement.value) > maxValue){
		alert(fieldName + " should not be greater than " + maxValue);
		txtElement.focus();
		return false;
	}

	txtElement.value=cent(round(txtElement.value,3));
	return true;
}

  //function to validate a TextArea
  function chkTxtArea(txtElement,maxAllowedLength,fieldName,allowEmpty)
  {
          if(allowEmpty == false && ltrim(txtElement.value).length == 0)
          {
                  alert("Please enter '" + fieldName + "'");
                  txtElement.focus();
                  return false;
          }
          if(txtElement.value.length > maxAllowedLength)
          {
                  alert("You have entered " + txtElement.value.length + " characters in the " + fieldName + ". \nThe Maximum number of characters allowed for this field is " + maxAllowedLength + ". \n" + fieldName + " is truncated to " + maxAllowedLength + " characters.");
                  //Truncate Statement
                  txtElement.value =
txtElement.value.substring(-1,maxAllowedLength);
                  return false;
          }
          return true;
  }

//function to validate a TextArea
/*
function chkTxtArea(txtElement,maxAllowedLength,fieldName,allowEmpty)
{
	if(allowEmpty == false && ltrim(txtElement.value).length == 0)
	{
		alert("Please enter '" + fieldName + "'");
		txtElement.focus();
		return false;
	}
	if(txtElement.value.length > maxAllowedLength)
	{
		//alert("You have entered " + txtElement.value.length + " characters in the " + fieldName + ". \nThe Maximum number of characters allowed for this field is " + maxAllowedLength + ", rest of the characters will be truncated.");
		if(!confirm("You have entered " + txtElement.value.length + " characters in the " + fieldName + ". \nThe Maximum number of characters allowed for this field is " + maxAllowedLength + ", rest of the characters will be truncated. \n Do you wish to continue?"))
		{
			txtElement.focus();
			return false;
		}

		//Truncate Statement
		txtElement.value = txtElement.value.substring(-1,maxAllowedLength);
		//return true;
	}
	return true;
}
*/

//function to validate a TextBox Input
function chkTxtBox(txtElement,fieldName)
{
	if(ltrim(txtElement.value).length == 0)
	{
		alert("'" + fieldName + "'");
		txtElement.focus();
		return false;
	}
	return true;
}

//checking if a checkbox is selected
function chkCheckBox(chkElement,fieldName,isAlert)
{
	if(isNaN(chkElement.length)) //ie if its not a group
	{
		if(!chkElement.checked)
		{
			if (isAlert)
			{
				alert("Please select  '" + fieldName + "'")
			}
			return false;
		}
		else
		{
			return true;
		}
	}
	else      //ie if it is a group
	{	var isChecked=false
		for(i=0;i<chkElement.length;i++)  //
		{
			isChecked = isChecked || chkElement[i].checked
		}
		if (!isChecked)
		{
			if (isAlert)
			{
				alert("Please choose  '" + fieldName + "'")
			}
				return false;
		}
		else
		{
			return true;
		}
	}
}


function chkRadio(optElement,fieldName)
{
	if(isNaN(optElement.length))
	{
		if(!optElement.checked)
		{
			alert("Please select a '" + fieldName + "'")

			return false;
		}
		else
		{
			return true;
		}
	}
	else
	{	var isChecked=false
		for(i=0;i<optElement.length;i++)  //
		{
			isChecked = isChecked || optElement[i].checked

		}
		if (!isChecked)
		{
			alert("Please select a '" + fieldName + "'")
			optElement[0].focus();
			return false
		}
		else
		{
			return true;
		}
	}
}


//checking if a dropdown listbox is selected.
//ASSUMPTION: a dropdown  listbox i considered to be not selected if its first element is currently selected
function chkDDList(lstElement,fieldName)
{
	if (lstElement.selectedIndex<=0)
	{
		alert("Please select '" + fieldName + "'")
		lstElement.focus()
		return false;
	}
	else
	{
		return true;
	}
}


// When date is passed as sting in a single text box
function chkDDMMYYString(txtElement,fieldName,allowEmpty)
{
	if(allowEmpty == false)
	{
		if(ltrim(txtElement.value).length == 0)
		{
			alert("Please enter '" + fieldName + "'");
			txtElement.focus();
			return false;
		}

	}

	if( allowEmpty == true && (ltrim(txtElement.value).length == 0))
	{
		//empty value is allowed
		return true;
	}
	else
		{
			var i=0;
			var j=0;
			var strDate = txtElement.value;
			j= strDate.indexOf("/",i);
			//alert(j);
			var strMnth=strDate.substring(i,j);
			i=strMnth.length + 1;
			j= strDate.indexOf("/",i);
			var strDay=strDate.substring(i,j);
			//for checking valid year
			var isValidYearStr = strDate.substring(j+1, strDate.length);
			j=j+3;
			i=strDate.length
			var strYear=strDate.substring(j,i);
			strMnth--;
			dtDate=new Date(strYear,strMnth,strDay);
			var dtDay=dtDate.getDate();
			var dtMnth=dtDate.getMonth();
			var dtYear=dtDate.getYear();
			//alert(dtMnth +'-'+ dtDay  +'-'+ dtYear);
			//alert(strMnth+ '  --  ' +dtMnth);
			//for century year increment the strMnth
			if(dtYear == 0){

				if(parseInt(strDay) <= 29){
					dtDay = strDay;
					if(parseInt(strDay) == 29) strMnth++;
				}
			}

			if((strDay!=dtDay) || (strMnth!=dtMnth) || (strYear!=dtYear) || isNaN(dtYear) || (strYear == ""))
			{
				alert("Invalid '" + fieldName + "'. Please enter date as 'mm/dd/yyyy'.")
				txtElement.focus()
				return false;
			}
			//alert(parseInt(isValidYearStr));
			if(parseInt(isValidYearStr) < 1900 || isValidYearStr.search("[^0-9]") != -1){
				alert("Invalid Year in '" + fieldName + "'. Please enter date as 'mm/dd/yyyyy'.")
				txtElement.focus()
				return false;
			}
			return true;
		}
}


function isValidDate(strDate)
{
    
	if(trim(strDate).length > 0) 
    {
        var i=0;
        var j=0;       
        j= strDate.indexOf("/",i);
        //alert(j);
        var strMnth=strDate.substring(i,j);
        i=strMnth.length + 1;
        j= strDate.indexOf("/",i);
        var strDay=strDate.substring(i,j);
        //for checking valid year
        var isValidYearStr = strDate.substring(j+1, strDate.length);
        j=j+3;
        i=strDate.length
        var strYear=strDate.substring(j,i);
        strMnth--;
        dtDate=new Date(strYear,strMnth,strDay);
        var dtDay=dtDate.getDate();
        var dtMnth=dtDate.getMonth();
        var dtYear=dtDate.getYear();
        //alert(dtMnth +'-'+ dtDay  +'-'+ dtYear);
        //alert(strMnth+ '  --  ' +dtMnth);
        //for century year increment the strMnth
        if(dtYear == 0){
            if(parseInt(strDay) <= 29){
                dtDay = strDay;
                if(parseInt(strDay) == 29) strMnth++;
            }
        }

        if((strDay!=dtDay) || (strMnth!=dtMnth) || (strYear!=dtYear) || isNaN(dtYear) || (strYear == ""))
        {
            //alert("Invalid '" + fieldName + "'. Please enter date as 'mm/dd/yyyy'.")           
            return false;
        }        
        if(parseInt(isValidYearStr) < 1900 || isValidYearStr.search("[^0-9]") != -1){
            //alert("Invalid Year in '" + fieldName + "'. Please enter date as 'mm/dd/yyyyy'.")           
            return false;
        }
        return true;
    }
    else {
        return false;
    }
		
}





//*****
// This file is used for checking, is user changed data or not.
// If User changed data or added data and he goes other module without saving data, 
// then he got a confirm messaage box, to ask user "Would you like to save data?"
// If user click on "OK", then he will going next page wherever he want to go
// If user click on "CANCEL", then page will not loaded any action.
//*****

var isGlobalTxtChanged = false;
var tmpTxtVal = "";


//**This function is used to change page location
function ChangePage()
{
	if(isGlobalTxtChanged)
	{
		if(confirm("You have not saved the changes to the data. Any unsaved data would be lost. Do you wish to continue anyway?"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		return true;
	}
}


function checkDecimals(field) {

    var fieldName = field.name;
    var fieldValue = field.value;
    decallowed = 2;  // how many decimals are allowed?
    
    if (isNaN(fieldValue) || fieldValue == "") {
    alert("That does not appear to be a valid number.  Please try again.");
    field.select();
    field.focus();
    }
    else {
    if (fieldValue.indexOf('.') == -1) fieldValue += ".";
    dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
    
    if (dectext.length > decallowed)
    {
    alert ("Please enter a number with up to " + decallowed + " decimal places.  Please try again.");
    field.select();
    field.focus();
          }
    else {
    //alert ("That number validated successfully.");
          }
       }
}

function blockSpecialCharacters()
{
	if ((event.keyCode > 32 && event.keyCode < 48) || (event.keyCode > 57 && event.keyCode < 65) || 
		(event.keyCode > 90 && event.keyCode < 97)) 
		event.returnValue = false;
}

function acceptOnlyNumbers()
{
	blockSpecialCharacters();
	if(event.keyCode < 45 || event.keyCode > 57) 
		event.returnValue = false;
}

function acceptOnlyFractionalDigits()
{
	blockSpecialCharacters();
	if(event.keyCode < 45 || event.keyCode > 57) 
		event.returnValue = false;
	else if(event.keyCode ==46)
		event.returnValue = true;
}

function onlyDigitsAndPoint(e) {
    var _ret = true;
    var reg = /\./g;    
    
    // make sure only digit number, negative sign (45) and one dot at most can be entered.
    // 47 is backslash, 46 is dot		
    if (window.event.keyCode < 46 || window.event.keyCode > 57 || window.event.keyCode == 47 || (window.event.keyCode == 46 && reg.test(window.event.srcElement.value))) {
        window.event.keyCode = 0;
        _ret = false;
    }
    
    
    return (_ret);
}

function acceptOnlyNumbersCOT(tagID)
{
	blockSpecialCharacters();
	if(event.keyCode < 45 || event.keyCode > 57) 
		event.returnValue = false;
	else if(event.keyCode ==46)
		event.returnValue = true;
        
    var fld  = document.getElementById(tagID);
    var temp_value = fld.value;         
    var fldVal = new NumberFormat(temp_value);
	fldVal.setPlaces(0);
    fld.value = fldVal.toUnFormatted();        
}

function checkPerCentages(field)
{
	var val = eval("document.getElementById('"+field+"')")
		if(val.value =='' || (val.value >=0 && val.value <=100))
			return true;
		else
			alert('The percentage '+val.value+' entered is Invalid');
//	val.value ='';
	val.focus();
    val.select();
	return false;
}


function checkPerCentagesNetUI(tagID)
{
	var val = eval("document.getElementById(getNetuiTagName('"+tagID+"'))")
		if(val.value =='' || (val.value >=0 && val.value <=100))
			return true;
		else
			alert('The percentage '+val.value+' entered is Invalid');
//	val.value ='';
	val.focus();
    val.select();    
	return false;
}

function formatCurrencyNetUI(tagID) {

    var fieldVal = eval("document.getElementById(getNetuiTagName('"+tagID+"'))")
    num = fieldVal.val;
    alert(num);

    blockSpecialCharacters();
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
    num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
    cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
    num = num.substring(0,num.length-(4*i+3))+','+
    num.substring(num.length-(4*i+3));
    return (((sign)?'':'-') + num + '.' + cents);
}


function validateCurrencyNetUI( tagID ) 
{ 
    var fld = eval("document.getElementById(getNetuiTagName('"+tagID+"'))")
   var temp_value = fld.value; 

   if (temp_value == "") 
   { 
     return; 
   } 
   var Chars = "0123456789.,"; 
   for (var i = 0; i < temp_value.length; i++) 
   { 
       if (Chars.indexOf(temp_value.charAt(i)) == -1) 
       { 
           alert("Invalid Character(s)\n\nOnly numbers (0-9), a comma and a period are allowed in the currency field."); 
           fld.focus(); 
       fld.select(); 
           return; 
       } 
   }
if(!isNaN(temp_value))      
   convertToUSD(fld,tagID,temp_value);
    
} 

function convertToUSD(fld,tagID,curency){

            var displayFld    = eval("document.getElementById('"+tagID+"Text')");
                displayFld.innerHTML = new NumberFormat(round((curency)  * (pspCurrencyExchangeRate))).toFormatted();                       
}

function convertToUSDCannibalization(fld,tagID,curency){
            var displayFld    = eval("document.getElementById('"+tagID+"Text')");
            var displayFldVal = new NumberFormat(round((curency)  / (pspCurrencyExchangeRate)));    
            displayFldVal.setPlaces(0);   
                displayFld.innerHTML = displayFldVal.toFormatted();                                       
}

function comparatorSalesInUSD(tagID,curency){

            var displayFld    = eval("document.getElementById('"+tagID+"Text')");
            var displayFldVal = new NumberFormat(round((curency)  / (pspCurrencyExchangeRate)));
            displayFldVal.setPlaces(0);               
            displayFld.innerHTML = displayFldVal.toFormatted()+" USD";                       
}

function validateCurrency( tagID ) 
{ 
   var fld  = document.getElementById(tagID);
   var temp_value = fld.value; 

   if (temp_value == "") 
   { 
     return; 
   } 
   var Chars = "0123456789.,"; 
   for (var i = 0; i < temp_value.length; i++) 
   { 
       if (Chars.indexOf(temp_value.charAt(i)) == -1) 
       { 
           alert("Invalid Character(s)\n\nOnly numbers (0-9), a comma and a period are allowed in the currency field."); 
           fld.focus(); 
       fld.select(); 
           return; 
       } 
   }
   var netuiFld     = eval("document.getElementById(getNetuiTagName('"+tagID+"'))")
   netuiFld.value   = temp_value;
   
   var displayFld    = eval("document.getElementById('"+tagID+"Text')");
if(!isNaN(temp_value))      
   convertToUSD(fld,tagID,temp_value);
   fld.value = new NumberFormat(temp_value).toFormatted();
} 

function validateComparatorSales( tagID ) 
{ 
   var fld  = document.getElementById(tagID);
   var temp_value = fld.value; 
   var displayFld    = eval("document.getElementById('"+tagID+"Text')");

   var marketSalesName = fld.name;
   marketSalesName  = marketSalesName.substring(0,marketSalesName.lastIndexOf("#"));
   var marketSales = eval("document.getElementById('"+marketSalesName+"')");
               
   if (temp_value == "") 
   { 
     marketSales.value='';
     displayFld.innerHTML=' USD';
     return; 
   } 
   var Chars = "0123456789.,"; 
   for (var i = 0; i < temp_value.length; i++) 
   { 
       if (Chars.indexOf(temp_value.charAt(i)) == -1) 
       { 
           alert("Invalid Character(s)\n\nOnly numbers (0-9), a comma and a period are allowed in the currency field."); 
           fld.focus(); 
       fld.select(); 
           return; 
       } 
   }
   

   	if(!isNaN(temp_value))   
        marketSales.value = temp_value;
if(!isNaN(temp_value))           
   comparatorSalesInUSD(tagID,temp_value);
   	if(!isNaN(temp_value))   
    {
        var fldVal = new NumberFormat(temp_value);
        fldVal.setPlaces(0);    
        fld.value = fldVal.toFormatted();
        }
} 

function validateCannibalization( tagID ) 
{ 
   var fld  = document.getElementById(tagID);
   var temp_value = fld.value; 

   if (temp_value == "") 
   { 
     return; 
   } 
   var Chars = "0123456789.,"; 
   for (var i = 0; i < temp_value.length; i++) 
   { 
       if (Chars.indexOf(temp_value.charAt(i)) == -1) 
       { 
           alert("Invalid Character(s)\n\nOnly numbers (0-9), a comma and a period are allowed in the currency field."); 
           fld.focus(); 
       fld.select(); 
           return; 
       } 
   }
   
   var marketSalesName = fld.name;
   marketSalesName  = marketSalesName.substring(0,marketSalesName.lastIndexOf("#"));
   var marketSales = eval("document.getElementById('"+marketSalesName+"')");
   	if(!isNaN(temp_value))   
        marketSales.value = temp_value;
   var displayFld    = eval("document.getElementById('"+tagID+"Text')");
if(!isNaN(temp_value))      
   convertToUSDCannibalization(fld,tagID,temp_value);
   	if(!isNaN(temp_value))   
    {        
        var fldVal = new NumberFormat(temp_value);
        fldVal.setPlaces(0);
        fld.value = fldVal.toFormatted();        
        }
} 

function validateComparatorInfo( tagID,start,end,pricePerDayFlag ) 
{ 
   var fld  = document.getElementById(tagID);
   var temp_value = fld.value; 

   if (temp_value == "") 
   { 
     return; 
   } 
   var Chars = "0123456789."; 
   for (var i = 0; i < temp_value.length; i++) 
   { 
       if (Chars.indexOf(temp_value.charAt(i)) == -1) 
       { 
           alert("Invalid Character(s)\n\nOnly numbers (0-9) and a period are allowed in the currency field."); 
           fld.focus(); 
       fld.select(); 
           return; 
       } 
   }

   var marketSalesName = fld.name;
   marketSalesName  = marketSalesName.substring(0,marketSalesName.lastIndexOf("#"));
   var marketSales = eval("document.getElementById('"+marketSalesName+"')");
   	if(!isNaN(temp_value))
	   marketSales.value = temp_value;   
   
   calc(start,end,pricePerDayFlag);
	if(!isNaN(temp_value))
	   fld.value = new NumberFormat(temp_value).toFormatted();
} 

function validateCurrency000( tagID )
{
   var fld  = document.getElementById(tagID);
   var temp_value = fld.value; 
   var displayFld    = eval("document.getElementById('"+tagID+"Text')");
   var netuiFld     = eval("document.getElementById(getNetuiTagName('"+tagID+"'))")
     
   if(isNaN(temp_value))
	{
	  val= parseFloat(temp_value);
	  var sign= ( val < 0 ? '-': '' );
	  val= Number(Math.round(Math.abs(val)*Math.pow(10,0))).toString();
	  while(val.length < 0) val= '0'+val;
	  var len= val.length;
	  val= sign + ( len == 0 ? '0' : val.substring(0,len-0) ) + val.substring(len-0,len+1);
	  temp_value= val;
	}
    
   if (temp_value == "") 
   { 
     displayFld.innerHTML='';
     netuiFld.value ='';
     return; 
   } 
        
   var Chars = "0123456789."; 
   for (var i = 0; i < temp_value.length; i++) 
   { 
       if (Chars.indexOf(temp_value.charAt(i)) == -1) 
       { 
           alert("Invalid Character(s)\n\nOnly numbers (0-9) and a period are allowed in the currency field."); 
           fld.focus(); 
       fld.select(); 
           return; 
       } 
   }

   netuiFld.value   = temp_value;
   

if(!isNaN(temp_value))      
   convertToUSDSalesMarketing(fld,tagID,temp_value);
    var fldVal = new NumberFormat(temp_value);
	fldVal.setPlaces(0);
    fld.value = fldVal.toFormatted();
}

function textCounter(field) {
var fld  = document.getElementById(field);

    if (fld.value.length > 1023) 
        fld.value = fld.value.substring(0, 1023);
}

function text4000Counter(field) {
var fld  = document.getElementById(field);

    if (fld.value.length > 3999) 
        fld.value = fld.value.substring(0, 3999);
}

function textNetUICounter(field) {
	var fld = eval("document.getElementById(getNetuiTagName('"+field+"'))")

    if (fld.value.length > 1023) 
        fld.value = fld.value.substring(0, 1023);
}

function textNetUI256Counter(field) {
	var fld = eval("document.getElementById(getNetuiTagName('"+field+"'))")

    if (fld.value.length > 255) 
        fld.value = fld.value.substring(0, 255);
}

function textNetUI4000Counter(field) {
	var fld = eval("document.getElementById(getNetuiTagName('"+field+"'))")

    if (fld.value.length > 3999) 
        fld.value = fld.value.substring(0, 3999);
}


function textNetUI3750Counter(field) {
	var fld = document.getElementById(field);

    if (fld.value.length > 3750) 
        fld.value = fld.value.substring(0, 3750);
}

function convertToUSDSalesMarketing(fld,tagID,curency){

            var displayFld    = eval("document.getElementById('"+tagID+"Text')");
            var displayFldVal = new NumberFormat(Math.floor((curency)  / (pspCurrencyExchangeRate)));    
            displayFldVal.setPlaces(0);   
                displayFld.innerHTML = displayFldVal.toFormatted();                       
}

  function getMonth(val) { 
    var monthArray = new Array("JAN","FEB","MAR","APR","MAY","JUN", 
                               "JUL","AUG","SEP","OCT","NOV","DEC"); 
    for (var i=0; i<monthArray.length; i++) { 
      if (monthArray[i].toLowerCase() == val.toLowerCase()) { 
        return(i); 
      } 
    } 
    return(-1); 
  } 

  function checkDate(txtElement,fieldName,allowEmpty) { 

	if(allowEmpty == false)
	{
		if(ltrim(txtElement.value).length == 0)
		{
			alert("Please enter '" + fieldName + "'");
			txtElement.focus();
			return false;
		}

	}

	if( allowEmpty == true && (ltrim(txtElement.value).length == 0))
	{
		//empty value is allowed
		return true;
	}

    var success = true; 
    var mo, day, yy, testDate; 
    var val = txtElement.value; 

    var re = new RegExp("[0-9]{1,2}[/-][A-Z]{3}[/-][0-9]{4}", "i"); 
    if (re.test(val)) { 
//      var delimChar = (val.indexOf("/") != -1) ? "/" : "-"; 
      var delimChar = "-"; 
      var delim1 = val.indexOf(delimChar); 
      var delim2 = val.lastIndexOf(delimChar); 
      day = parseInt(val.substring(0,delim1),10); 
      mo = getMonth(val.substring(delim1+1,delim2),10); 
      yy = parseInt(val.substring(delim2+1),10); 
      testDate = new Date(yy,mo,day); 
      if (testDate.getDate() == day) { 
        if (testDate.getMonth() == mo) { 
          if (!testDate.getFullYear() == yy) { 
            alert("Invalid year entry."); 
            success = false; 
          } 
        } 
        else { 
          alert("Invalid month entry."); 
          success = false; 
        } 
      } 
      else { 
        alert("Invalid day entry."); 
        success = false; 
      } 
    } 
    else { 
      alert("Incorrect date format.  Enter as DD-MON-YYYY."); 
      success = false; 
    } 

    if (!success) txtElement.focus(); 

    return(success); 
  } 

function fixFixed(fld,dec,sep)
{ 
 var formField = eval("document.getElementById('"+fld.name+"')");
	// fixed decimal fields 
  if(!fld.value.length||fld.disabled) return true; // blank fields are the domain of requireValue 
  var val= fld.value;
  if(typeof(sep)!='undefined') val= val.replace(new RegExp(sep,'g'),'');
  val= parseFloat(fld.value);
  
  if(isNaN(val))
  { // parse error 
    alert('The field must contain a number.');
	formField.select();
	formField.focus();
    return false;
  }

  var sign= ( val < 0 ? '-': '' );
  val= Number(Math.round(Math.abs(val)*Math.pow(10,dec))).toString();
  while(val.length < dec) val= '0'+val;
  var len= val.length;
  val= sign + ( len == dec ? '0' : val.substring(0,len-dec) ) + '.' + val.substring(len-dec,len+1);
  fld.value= val;
  return true;
}

//Added by Mahua Ghosh :: 15Dec2011
//To simulate the replaceAll function in javascript
function replaceAll(txt, replace, with_this) {
  //alert(txt);
  //alert(replace);
  //alert(with_this);
  return txt.replace(new RegExp(replace, 'g'),with_this);
}
