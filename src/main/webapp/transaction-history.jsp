<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    
    <title>Transaction History</title>
    
    <!-- Include jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- Include Bootstrap JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Font Icon -->
    <link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css">
        
    <!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Main CSS -->
    <link rel="stylesheet" href="css/style.css">    
    
    <script>
        $(document).ready(function() {
            $.ajax({
                url: "ServletTransactions",
                method: "GET",
                dataType: "json",
                success: function(data) {
                    // Parse the JSON data and create HTML to display it
 					var table = $("<table>").attr("style", "border: 1px solid #000; width: 100%;");                    
 					table.append("<thead><tr><th style='border: 1px solid #000; padding: 5px;'>Transaction Id</th><th style='border: 1px solid #000; padding: 5px;'>Transaction Type</th><th style='border: 1px solid #000; padding: 5px;'>Amount</th><th style='border: 1px solid #000; padding: 5px;'>Date / Time</th></tr></thead>");                    
 					var tbody = $("<tbody>");
                    $.each(data, function(index, transaction) {
                        var row = $("<tr>");
                        row.append("<td style='border: 1px solid #000; padding: 5px;'>" + transaction.transactionId + "</td>");
                        row.append("<td style='border: 1px solid #000; padding: 5px;'>" + transaction.transactionType + "</td>");
                        row.append("<td style='border: 1px solid #000; padding: 5px;'>"+ transaction.amount + "</td>");
                        row.append("<td style='border: 1px solid #000; padding: 5px;'>" + transaction.date + "</td>");
                        tbody.append(row);
                    });
                    table.append(tbody);
                    $("#transactionTable").empty().append(table);
                },
                error: function() {
                    $("#transactionTable").text("Error fetching transaction history.");
                }
            });
        });
    </script>
</head>

<body>
    <jsp:include page="header.jsp" />
    <div class="main" style="background: #8ad0da69; padding-bottom: 30%">
        <section class="signup">
            <div class="container" style="margin-top: 10%; background: #4b6e7942;">
                <div class="signup-content">
                    <div class="signup-form" style="width: 100%;">
                        <h2 class="form-title">Transaction History</h2>
                        <div id="transactionTable">
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</body>
</html>