SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==== TABLES ====

-- partner
CREATE TABLE partner (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  status      ENUM('PENDING','ACTIVE','SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- app_user
CREATE TABLE app_user (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  email       VARCHAR(255) NOT NULL,
  phone       VARCHAR(32)  NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_app_user_email (email),
  UNIQUE KEY uq_app_user_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- theatre
CREATE TABLE theatre (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  partner_id   BIGINT NOT NULL,
  name        VARCHAR(255) NOT NULL,
  city        VARCHAR(128) NOT NULL,
  address     VARCHAR(512),
  status      ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE KEY uq_theatre_partner_id (partner_id, id),

  CONSTRAINT fk_theatre_partner
    FOREIGN KEY (partner_id) REFERENCES partner(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- screen
CREATE TABLE screen (
  id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  theatre_id   BIGINT NOT NULL,
  name         VARCHAR(128) NOT NULL,
  seat_layout  JSON NOT NULL,
  seat_count  INT,
  created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_screen_theatre
    FOREIGN KEY (theatre_id) REFERENCES theatre(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- performance
CREATE TABLE performance (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  screen_id   BIGINT NOT NULL,
  start_at    DATETIME NOT NULL,
  end_at      DATETIME,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_perf_screen
    FOREIGN KEY (screen_id) REFERENCES screen(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE seat_lock (
  id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  show_id      BIGINT NOT NULL,
  seat_label   VARCHAR(32) NOT NULL,
  hold_from    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  hold_until   DATETIME NOT NULL,
  status       VARCHAR(32) NOT NULL,
  created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_seat_lock_show
    FOREIGN KEY (show_id) REFERENCES performance(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_seat_lock_show ON seat_lock (show_id);
CREATE INDEX idx_seat_lock_until ON seat_lock (hold_until);

-- booking
CREATE TABLE booking (
  id            BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  show_id       BIGINT NOT NULL,
  user_id       BIGINT NOT NULL,
  subtotal      INT NOT NULL,
  discount_code VARCHAR(32),
  discount      INT NOT NULL DEFAULT 0,
  taxes_fees    INT NOT NULL DEFAULT 0,
  total         INT NOT NULL,

  status        ENUM('PENDING_PAYMENT','CONFIRMED','CANCELLED','FAILED','EXPIRED')
                  NOT NULL DEFAULT 'PENDING_PAYMENT',

  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_booking_show FOREIGN KEY (show_id) REFERENCES performance(id),
  CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES app_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_booking_show   ON booking (show_id);
CREATE INDEX idx_booking_user   ON booking (user_id);
CREATE INDEX idx_booking_status ON booking (status);

-- booked_seat
CREATE TABLE booked_seat (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  booking_id  BIGINT NOT NULL,
  seat_label  VARCHAR(32) NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_bi_booking
    FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_bi_booking ON booked_seat (booking_id);

-- payment
CREATE TABLE payment (
  id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  partner_id   BIGINT NOT NULL,
  booking_id  BIGINT NOT NULL,
  transaction_id VARCHAR(128) NOT NULL,
  provider    VARCHAR(264) NOT NULL,
  amount      INT NOT NULL,
  status      ENUM('INITIATED','SUCCEEDED','FAILED','REFUNDED','PARTIAL_REFUNDED')
                NOT NULL DEFAULT 'INITIATED',
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_payment_partner_id
    FOREIGN KEY (partner_id)  REFERENCES partner(id) ON DELETE CASCADE,
  CONSTRAINT fk_payment_booking
    FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- SAMPLE DATA SEED
-- Run after tables are created. Adjust times/IDs as you like.

START TRANSACTION;

-- Set to IST (matches your app config)
SET time_zone = '+05:30';

-- ===== PARTNER =====
INSERT INTO partner (id, name, status)
VALUES
  (1, 'INOX', 'ACTIVE'),
  (2, 'PVR',  'ACTIVE');

-- ===== USERS =====
INSERT INTO app_user (id, email, phone)
VALUES
  (1, 'alice@example.com',  '+919900000001'),
  (2, 'bob@example.com',    '+919900000002'),
  (3, 'charlie@example.com','+919900000003');

-- ===== THEATRES =====
INSERT INTO theatre (id, partner_id, name, city, address, status)
VALUES
  (1, 1, 'INOX Phoenix Pune', 'Pune',   'Phoenix Marketcity, Viman Nagar', 'ACTIVE'),
  (2, 2, 'PVR Lower Parel',   'Mumbai', 'High Street Phoenix, Lower Parel', 'ACTIVE');

-- ===== SCREENS =====
-- Minimal JSON layout; seat_count is illustrative.
INSERT INTO screen (id, theatre_id, name, seat_layout, seat_count)
VALUES
  (1, 1, 'Screen 1',
     JSON_OBJECT('rows', JSON_OBJECT('A', 20, 'B', 20, 'C', 20, 'D', 20, 'E', 20)),
     100),
  (2, 2, 'Gold 2',
     JSON_OBJECT('rows', JSON_OBJECT('A', 10, 'B', 10, 'C', 10, 'D', 10, 'E', 10, 'F', 10, 'G', 10, 'H', 10)),
     80);

-- ===== PERFORMANCES (SHOWS) =====
-- Use fixed times so you can test reliably.
INSERT INTO performance (id, screen_id, start_at, end_at)
VALUES
  (1, 1, '2025-09-07 19:30:00', '2025-09-07 22:30:00'),
  (2, 1, '2025-09-08 16:00:00', '2025-09-08 19:00:00'),
  (3, 2, '2025-09-07 20:00:00', '2025-09-07 23:00:00');

-- ===== SEAT LOCKS (temporary holds) =====
-- status is free-form; using HELD / RELEASED.
INSERT INTO seat_lock (id, show_id, seat_label, hold_from, hold_until, status)
VALUES
  (1, 1, 'A10', NOW(), DATE_ADD(NOW(), INTERVAL 10 MINUTE), 'HELD'),
  (2, 1, 'A11', DATE_SUB(NOW(), INTERVAL 15 MINUTE), DATE_SUB(NOW(), INTERVAL 5 MINUTE), 'EXPIRED');

-- ===== BOOKINGS =====
-- Booking 1: confirmed; Booking 2: pending payment; Booking 3: cancelled.
INSERT INTO booking
(id, show_id, user_id, subtotal, discount_code, discount, taxes_fees, total, status)
VALUES
  (1, 1, 1, 700, 'WELCOME10', 50, 100, 750, 'CONFIRMED'),
  (2, 3, 2, 450, NULL,        0,  50, 500, 'PENDING_PAYMENT'),
  (3, 1, 3, 800, 'FEST20',    20, 80, 860, 'FAILED');

-- ===== BOOKED SEATS =====
INSERT INTO booked_seat (id, booking_id, seat_label)
VALUES
  (1, 1, 'A10'),
  (2, 1, 'A11'),
  (3, 2, 'B01'),
  (4, 3, 'A12');

-- ===== PAYMENTS =====
-- For booking 1: succeeded via Razorpay (partner 1).
-- For booking 2: initiated via Cashfree (partner 2).
-- For booking 3: refunded (demonstrates another status).
INSERT INTO payment
(id, partner_id, booking_id, transaction_id, provider, amount, status)
VALUES
  (1, 1, 1, 'rzp_Txn_10001', 'Razorpay', 750, 'SUCCEEDED'),
  (2, 2, 2, 'cf_Txn_20001',  'Cashfree', 500, 'FAILED'),
  (3, 1, 3, 'rzp_Txn_10002', 'Razorpay', 860, 'PENDING');

COMMIT;
