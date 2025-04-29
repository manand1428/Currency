async function loadCurrencies() {
    try {
        const res = await fetch('/currency/currencies'); // Call your backend
        const currencies = await res.json();

        const fromSelect = document.getElementById('sourceCurrency');
        const toSelect = document.getElementById('targetCurrency');

        fromSelect.innerHTML = '';
        toSelect.innerHTML = '';

        currencies.forEach(currencyCode => {
            const option1 = document.createElement('option');
            option1.value = currencyCode;
            option1.textContent = currencyCode;
            fromSelect.appendChild(option1);

            const option2 = document.createElement('option');
            option2.value = currencyCode;
            option2.textContent = currencyCode;
            toSelect.appendChild(option2);
        });

        fromSelect.value = "USD";
        toSelect.value = "AUD";
    } catch (error) {
        console.error('Failed to load currencies', error);
    }
}

// Call when page loads
loadCurrencies();

function convertCurrency() {
    const sourceCurrency = document.getElementById('sourceCurrency').value;
    const targetCurrency = document.getElementById('targetCurrency').value;
    const amount = document.getElementById('amount').value;
    if(!amount || amount <= 0){
        const validation = document.getElementById('validation');
        validation.innerHTML = "Please Enter Amount";
        validation.style.display = "block";

        setTimeout(() =>{
            validation.style.display = "none;"
        },2000);
        return;
    }

    fetch(`http://localhost:8080/currency/convert?sourceCurrency=${sourceCurrency}&targetCurrency=${targetCurrency}&amount=${amount}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server error: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Conversion Response:", data);
            document.getElementById('convertedAmount').textContent = data.convertedAmount.toFixed(2)+' '+data.targetCurrency;
        })
        .catch(error => {
            console.error("Error during conversion:", error);
            alert('Conversion failed: ' + error.message);
        });
}


