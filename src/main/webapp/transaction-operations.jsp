<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	
	<title>Transaction Operations</title>
	
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
</head>

<body>
<%
    HttpServletRequest newRequest = (HttpServletRequest) pageContext.getRequest();

    Cookie[] cookies = newRequest.getCookies();

    String usernameCookieName = "username";

    String usernameCookieValue = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(usernameCookieName)) {
                usernameCookieValue = cookie.getValue();
            }  
        }
    }

    if (usernameCookieValue == null) {
    	 response.sendRedirect("login.jsp");
    }
    
%>
<jsp:include page="header.jsp" />
	<div class="main" style="background: #8ad0da69">
		<!-- Account form -->
		<section class="signup">
			<div class="container" style="margin-top: 10%; background: #9ed7a457;">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title">Transaction Operations</h2>
						<form method="post" class="register-form" action="ServletTransactions" id="register-form">
							<div class="form-group">
								<select class="form-control" id="transerType" name="transerType" onchange="toggleAccountNumberField()">
									<option value="default">Select transaction type</option>
	                                <option value="deposit">Deposit</option>
	                                <option value="withdraw">Withdraw</option>
	                             </select>
							</div>
							<div class="form-group">
								<label for="account-number"><i class="zmdi zmdi-card zmdi-hc-2x px-1"></i></label> 
								<input type="number" name="account-number" id="account-number" placeholder="Enter account number." />
							</div>
							<div class="form-group">
								<label for="amount"><i class="zmdi zmdi-money-box zmdi-hc-2x px-1"></i></label> 
								<input type="number" name="amount" id="amount" step="0.01" min="0.01" placeholder="Enter amount." />
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signup" id="signup"
									class="form-submit" value="Submit" />
							</div>
						</form>
					</div>
					<div class="signup-image">
						<figure>
							<img src="images/transaction-operations.png" alt="sing up image">
						</figure>
					</div>
				</div>
			</div>
		</section>
	</div>
	<!-- JavaScript code -->
    <script>
        function toggleAccountNumberField() {
            var transactionType = document.getElementById("transerType").value;
            var accountNumberField = document.getElementById("account-number");

            if (transactionType === "withdraw") {
                // Disable the field when "Withdraw" is selected
                accountNumberField.disabled = true;
            } else {
                // Enable the field for other transaction types
                accountNumberField.disabled = false;
            }
        }
    </script>
</body>
</html>
