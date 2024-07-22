CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    user_uuid UUID DEFAULT gen_random_uuid() NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE desks (
    desk_id SERIAL PRIMARY KEY,
    desk_uuid UUID DEFAULT gen_random_uuid() NOT NULL,
    location VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
    booking_id SERIAL PRIMARY KEY,
    booking_uuid UUID DEFAULT gen_random_uuid() NOT NULL,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    desk_id INT NOT NULL REFERENCES desks(desk_id) ON DELETE CASCADE,
    booking_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (desk_id, booking_date)
);

CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_desk_id ON bookings(desk_id);
CREATE INDEX idx_bookings_booking_date ON bookings(booking_date);


