var myDB;
document.addEventListener('deviceready', onDeviceReady, false);
function onDeviceReady(){
    myDB = window.openDatabase('myExpense.db','1.0','MExpense DB', 1000000);
    myDB.transaction(createDB, errorDB, successDB);
    select('');
    $('#entry').hide();
}

 function createDB(tx){
    tx.executeSql('CREATE TABLE IF NOT EXISTS tblTrip('
    +' id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, destination TEXT, trip_date TEXT,'
    +' risk_assessment INTEGER, description TEXT, value1 TEXT, value2 TEXT, value3 TEXT,'
    +' num1 REAL, num2 REAL)');
 }
 function select(keyword){
    myDB.transaction(function(tx){
        tx.executeSql("select * from tblTrip"
        +" where name LIKE '%"+keyword+"%'",[], dataSelect, errorDB);
    }, errorDB, successDB);
    $('#btnAdd').show();
    $('#detail').hide();
 }
 function dataSelect(tx, results){
    var divList = document.getElementById('list');
    var recordList = '';
    //<button class="ui-btn" onclick="getData()">OK</button>
    for(var i=0; i<results.rows.length; i++ ){
        var record = results.rows.item(i).name +'<br />'
                     + results.rows.item(i).destination +'<br />'
                     + results.rows.item(i).trip_date +'<br />'
                     +'<button class="ui-btn ui-icon-edit ui-btn-icon-notext ui-corner-all" onclick="onEdit('+results.rows.item(i).id+')"></button>'
                     +'<button class="ui-btn ui-icon-delete ui-btn-icon-notext ui-corner-all" onclick="deleteDB('+results.rows.item(i).id+')"></button>'
                     +'<button class="ui-btn ui-icon-eye ui-btn-icon-notext ui-corner-all" onclick="onDetail('+results.rows.item(i).id+')"></button>';
        recordList += '<div style="margin:8px; border-style: dotted; padding:8px;">' + record + '</div>';
    }
    divList.innerHTML = recordList;
 }
 function save(){
    if(myID){
        update( myID ); return; // goto update function
    }
    var name =document.getElementById('txtName').value;
    var destination =$('#txtDestination').val();
    var trip_date =$('#txtDate').val();
    var risk_assessment =$('#chkRiskAssessment').is(":checked");
    var description =$('#txtDescription').val();

    if( !name ){
        $("#txtName").focus();
        alert('Please enter the trip name!'); return;
    }
    if( !destination ){
        $("#txtDestination").focus();
        alert('Please enter the destination!'); return;
    }
    if( !trip_date ){
        $("#txtDate").focus();
        alert('Please enter the trip date!'); return;
    }

    myDB.transaction(function(tx){
        tx.executeSql('INSERT INTO tblTrip(name, destination, trip_date, risk_assessment, description) VALUES('
         + '"' + name + '",'
         + '"' + destination + '",'
         + '"' + trip_date + '",'
         + (risk_assessment===true? 1 : 0) + ','
         + '"' + description + '"'
         + ')');
        }, errorDB, successDB);
    onClose();
    select('');
 }

 function update(id){

    var name =document.getElementById('txtName').value;
    var destination =$('#txtDestination').val();
    var trip_date =$('#txtDate').val();
    var risk_assessment =$('#chkRiskAssessment').is(":checked");
    var description =$('#txtDescription').val();

    myDB.transaction(function(tx){
        tx.executeSql('UPDATE tblTrip SET '
         + 'name="' + name + '",'
         + 'destination="' + destination + '",'
         + 'trip_date="' + trip_date + '",'
         + 'risk_assessment=' + (risk_assessment===true? 1 : 0) + ','
         + 'description="' + description
         + '" where id=' + id);
        }, errorDB, successDB);
    onClose();
    select('');
 }

 function deleteDB(id){
    myDB.transaction(function(tx){
         tx.executeSql('DELETE FROM tblTrip where id=' + id);
     }, errorDB, successDB);
     select('');
 }
 function onSearch(){
    var txtSearch = document.getElementById('search-1');
    select( txtSearch.value );
 }
 function add(){
    $('#entry').show();
    $('#display').hide();
    $('#btnAdd').hide();
    $('#detail').hide();
    myID=null;
    document.getElementById('txtName').value ='';
    $('#txtDestination').val('');
    $('#txtDate').val('');
    description =$('#txtDescription').val('');
 }
 function onClose(){
    $('#display').show();
    $('#entry').hide();
    $('#btnAdd').show();
    $('#detail').hide();
 }

var myID=null;
function onEdit(id){
    myDB.transaction(function(tx){
        tx.executeSql("select * from tblTrip"
        +" where id="+id,[],  function(tx, results){
             for(var i=0; i<results.rows.length; i++ ){
                $('#txtName').val( results.rows.item(i).name );
                $('#txtDestination').val( results.rows.item(i).destination );
                $('#txtDate').val( results.rows.item(i).trip_date );
                $('#txtDescription').val( results.rows.item(i).description );
                if( results.rows.item(i).risk_assessment === 1){
                    $('#chkRiskAssessment').click();
                }
             }
          }, errorDB);
    }, errorDB, successDB);
    add();
    myID = id;
}

function onDetail(id){

var divList = document.getElementById('detail');
    var recordList = '';

    myDB.transaction(function(tx){
        tx.executeSql("select * from tblTrip"
        +" where id="+id,[],  function(tx, results){

             for(var i=0; i<results.rows.length; i++ ){
             var record = 'Trip name: '+ results.rows.item(i).name +'<br />'
                                  + 'Destination: ' + results.rows.item(i).destination +'<br />'
                                  + 'Trip date: ' + results.rows.item(i).trip_date +'<br />'
                                  + 'Risk assessment: ' + ( results.rows.item(i).risk_assessment === 1? 'Yes' : 'No') +'<br />'
                                  + 'Description: ' + results.rows.item(i).description +'<br />'
                                  +'<button class="ui-btn" onclick="onClose()">Close</button>';
                     recordList += '<div style="margin:8px; border-style: dotted; padding:8px;">' + record + '</div>';
             }
             divList.innerHTML = recordList;

          }, errorDB);
    }, errorDB, successDB);

    $('#display').hide();
    $('#entry').hide();
    $('#btnAdd').hide();
    $('#detail').show();
}

 function successDB(){
    //alert('DB process success');
 }
 function errorDB(error){
    alert('Error:'+error);
 }