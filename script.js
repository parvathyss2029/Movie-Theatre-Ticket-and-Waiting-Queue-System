```javascript
const totalSeats = 10;

const availableSeats = {
    "Interstellar": 10,
    "Inception": 10,
    "The Dark Knight": 10
};

const waitingQueues = {
    "Interstellar": [],
    "Inception": [],
    "The Dark Knight": []
};

let bookingId = 1001;
const bookings = {};

function chooseMovie(movieName) {
    document.getElementById("movie").value = movieName;
    document.getElementById("booking").scrollIntoView({
        behavior: "smooth"
    });
}

document.getElementById("bookingForm").addEventListener(
    "submit",
    function(event) {
        event.preventDefault();

        const customer = document
            .getElementById("customer").value.trim();

        const movie = document.getElementById("movie").value;
        const message = document.getElementById("message");

        if (customer === "" || movie === "") {
            message.textContent =
                "Please enter your name and choose a movie.";
            return;
        }

        if (availableSeats[movie] > 0) {
            const id = bookingId++;

            availableSeats[movie]--;
            bookings[id] = {
                customer: customer,
                movie: movie
            };

            message.textContent =
                "Booking successful! Your Booking ID is " + id +
                ". Seats remaining: " + availableSeats[movie] + ".";

        } else {
            waitingQueues[movie].push(customer);

            message.textContent =
                "Sorry, this show is full. " + customer +
                " has been added to the waiting queue. Position: " +
                waitingQueues[movie].length + ".";
        }

        document.getElementById("customer").value = "";
    }
);
```
