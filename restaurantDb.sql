--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13 (Homebrew)
-- Dumped by pg_dump version 14.15 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: customers; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.customers (
    customerid integer NOT NULL,
    name character varying(100),
    contactinfo character varying(100),
    loyaltypoints integer DEFAULT 0
);


ALTER TABLE public.customers OWNER TO jamesbiser;

--
-- Name: customers_customerid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.customers_customerid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.customers_customerid_seq OWNER TO jamesbiser;

--
-- Name: customers_customerid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.customers_customerid_seq OWNED BY public.customers.customerid;


--
-- Name: employees; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.employees (
    employeeid integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20),
    name character varying(100),
    contactinfo character varying(100)
);


ALTER TABLE public.employees OWNER TO jamesbiser;

--
-- Name: employees_employeeid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.employees_employeeid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.employees_employeeid_seq OWNER TO jamesbiser;

--
-- Name: employees_employeeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.employees_employeeid_seq OWNED BY public.employees.employeeid;


--
-- Name: menuitems; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.menuitems (
    itemid integer NOT NULL,
    name character varying(100) NOT NULL,
    price numeric(10,2) NOT NULL,
    allergens text
);


ALTER TABLE public.menuitems OWNER TO jamesbiser;

--
-- Name: menuitems_itemid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.menuitems_itemid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.menuitems_itemid_seq OWNER TO jamesbiser;

--
-- Name: menuitems_itemid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.menuitems_itemid_seq OWNED BY public.menuitems.itemid;


--
-- Name: orderdetails; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.orderdetails (
    orderdetailid integer NOT NULL,
    orderid integer,
    itemid integer,
    quantity integer NOT NULL,
    price numeric(10,2) NOT NULL
);


ALTER TABLE public.orderdetails OWNER TO jamesbiser;

--
-- Name: orderdetails_orderdetailid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.orderdetails_orderdetailid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.orderdetails_orderdetailid_seq OWNER TO jamesbiser;

--
-- Name: orderdetails_orderdetailid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.orderdetails_orderdetailid_seq OWNED BY public.orderdetails.orderdetailid;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.orders (
    orderid integer NOT NULL,
    tablenumber integer NOT NULL,
    ordertime timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(20)
);


ALTER TABLE public.orders OWNER TO jamesbiser;

--
-- Name: orders_orderid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.orders_orderid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.orders_orderid_seq OWNER TO jamesbiser;

--
-- Name: orders_orderid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.orders_orderid_seq OWNED BY public.orders.orderid;


--
-- Name: payments; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.payments (
    paymentid integer NOT NULL,
    orderid integer,
    amount numeric(10,2) NOT NULL,
    paymentmethod character varying(50),
    paymenttime timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.payments OWNER TO jamesbiser;

--
-- Name: payments_paymentid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.payments_paymentid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payments_paymentid_seq OWNER TO jamesbiser;

--
-- Name: payments_paymentid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.payments_paymentid_seq OWNED BY public.payments.paymentid;


--
-- Name: reservations; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.reservations (
    reservationid integer NOT NULL,
    customername character varying(100) NOT NULL,
    partysize integer NOT NULL,
    reservationtime timestamp without time zone NOT NULL,
    contactinfo character varying(100)
);


ALTER TABLE public.reservations OWNER TO jamesbiser;

--
-- Name: reservations_reservationid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.reservations_reservationid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reservations_reservationid_seq OWNER TO jamesbiser;

--
-- Name: reservations_reservationid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.reservations_reservationid_seq OWNED BY public.reservations.reservationid;


--
-- Name: schedules; Type: TABLE; Schema: public; Owner: jamesbiser
--

CREATE TABLE public.schedules (
    scheduleid integer NOT NULL,
    employeeid integer,
    shifttime timestamp without time zone NOT NULL,
    jobrole character varying(50)
);


ALTER TABLE public.schedules OWNER TO jamesbiser;

--
-- Name: schedules_scheduleid_seq; Type: SEQUENCE; Schema: public; Owner: jamesbiser
--

CREATE SEQUENCE public.schedules_scheduleid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.schedules_scheduleid_seq OWNER TO jamesbiser;

--
-- Name: schedules_scheduleid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jamesbiser
--

ALTER SEQUENCE public.schedules_scheduleid_seq OWNED BY public.schedules.scheduleid;


--
-- Name: customers customerid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.customers ALTER COLUMN customerid SET DEFAULT nextval('public.customers_customerid_seq'::regclass);


--
-- Name: employees employeeid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.employees ALTER COLUMN employeeid SET DEFAULT nextval('public.employees_employeeid_seq'::regclass);


--
-- Name: menuitems itemid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.menuitems ALTER COLUMN itemid SET DEFAULT nextval('public.menuitems_itemid_seq'::regclass);


--
-- Name: orderdetails orderdetailid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.orderdetails ALTER COLUMN orderdetailid SET DEFAULT nextval('public.orderdetails_orderdetailid_seq'::regclass);


--
-- Name: orders orderid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.orders ALTER COLUMN orderid SET DEFAULT nextval('public.orders_orderid_seq'::regclass);


--
-- Name: payments paymentid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.payments ALTER COLUMN paymentid SET DEFAULT nextval('public.payments_paymentid_seq'::regclass);


--
-- Name: reservations reservationid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.reservations ALTER COLUMN reservationid SET DEFAULT nextval('public.reservations_reservationid_seq'::regclass);


--
-- Name: schedules scheduleid; Type: DEFAULT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.schedules ALTER COLUMN scheduleid SET DEFAULT nextval('public.schedules_scheduleid_seq'::regclass);


--
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.customers (customerid, name, contactinfo, loyaltypoints) FROM stdin;
1	John Doe	john.doe@example.com	50
2	Emily Clark	emily.clark@example.com	30
3	David Lee	david.lee@example.com	70
4	Sophia Brown	sophia.brown@example.com	10
\.


--
-- Data for Name: employees; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.employees (employeeid, username, password, role, name, contactinfo) FROM stdin;
1	jxb6500	Password123@	manager	James Biser	jxb6500@psu.edu
2	jkw6102	Password123@	manager	Jay Wang	jkw6102@psu.edu
3	asmith	Waiter123$	waiter	Alice Smith	asmith@example.com
4	bwhite	Chef123#	chef	Bob White	bwhite@example.com
5	dynamic_user	dynamic_password	\N	\N	\N
8	bestPrez	Trump	PRESIDENT	Donald Trump	TMoney@ONTOP.com
7	JBiden	joebiden	BadPrez	Joe Biden	joeBiden@OUTTIE.edu
9	joeSmo	joeSmo	joeSmo	joeSmo	joeSmo
\.


--
-- Data for Name: menuitems; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.menuitems (itemid, name, price, allergens) FROM stdin;
1	Pasta Carbonara	12.99	gluten, dairy
2	Caesar Salad	8.99	dairy
3	Grilled Chicken	14.99	none
4	Chocolate Cake	6.99	gluten, nuts, dairy
5	Vegan Burger	10.99	soy
6	Cheeseburger	8.99	Dairy, Gluten
7	Margherita Pizza	12.50	Dairy, Gluten
8	Caesar Salad	7.99	Dairy
9	Grilled Chicken Sandwich	9.99	Gluten
10	Vegetable Stir Fry	10.50	Soy
11	Seafood Paella	18.75	Shellfish
12	Chocolate Lava Cake	6.50	Dairy, Eggs
13	Vegan Burger	11.99	Gluten
14	Spaghetti Carbonara	13.50	Dairy, Eggs, Gluten
15	Buffalo Wings	8.75	Dairy
16	Waffles	12.50	Gluten
\.


--
-- Data for Name: orderdetails; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.orderdetails (orderdetailid, orderid, itemid, quantity, price) FROM stdin;
1	1	1	2	25.98
2	1	2	1	8.99
3	2	3	3	44.97
4	3	4	1	6.99
5	4	5	2	21.98
6	1	3	2	29.98
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.orders (orderid, tablenumber, ordertime, status) FROM stdin;
1	1	2024-12-08 12:15:00	active
2	2	2024-12-08 12:30:00	completed
3	3	2024-12-08 13:00:00	active
4	4	2024-12-08 13:30:00	active
\.


--
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.payments (paymentid, orderid, amount, paymentmethod, paymenttime) FROM stdin;
1	1	34.97	credit card	2024-12-08 12:45:00
2	2	44.97	cash	2024-12-08 13:00:00
3	3	6.99	credit card	2024-12-08 13:15:00
4	4	21.98	mobile payment	2024-12-08 13:45:00
\.


--
-- Data for Name: reservations; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.reservations (reservationid, customername, partysize, reservationtime, contactinfo) FROM stdin;
1	John Doe	4	2024-12-08 19:00:00	john.doe@example.com
2	Emily Clark	2	2024-12-08 20:00:00	emily.clark@example.com
3	David Lee	6	2024-12-08 21:00:00	david.lee@example.com
\.


--
-- Data for Name: schedules; Type: TABLE DATA; Schema: public; Owner: jamesbiser
--

COPY public.schedules (scheduleid, employeeid, shifttime, jobrole) FROM stdin;
1	1	2024-12-08 09:00:00	manager
3	3	2024-12-08 15:00:00	waiter
4	4	2024-12-08 18:00:00	chef
5	8	2024-01-20 12:00:00	Lead
2	2	2024-12-08 12:15:00	manager
6	7	2025-01-20 12:00:00	Pres
\.


--
-- Name: customers_customerid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.customers_customerid_seq', 4, true);


--
-- Name: employees_employeeid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.employees_employeeid_seq', 9, true);


--
-- Name: menuitems_itemid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.menuitems_itemid_seq', 16, true);


--
-- Name: orderdetails_orderdetailid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.orderdetails_orderdetailid_seq', 6, true);


--
-- Name: orders_orderid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.orders_orderid_seq', 4, true);


--
-- Name: payments_paymentid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.payments_paymentid_seq', 4, true);


--
-- Name: reservations_reservationid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.reservations_reservationid_seq', 3, true);


--
-- Name: schedules_scheduleid_seq; Type: SEQUENCE SET; Schema: public; Owner: jamesbiser
--

SELECT pg_catalog.setval('public.schedules_scheduleid_seq', 6, true);


--
-- Name: customers customers_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (customerid);


--
-- Name: employees employees_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (employeeid);


--
-- Name: employees employees_username_key; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_username_key UNIQUE (username);


--
-- Name: menuitems menuitems_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.menuitems
    ADD CONSTRAINT menuitems_pkey PRIMARY KEY (itemid);


--
-- Name: orderdetails orderdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_pkey PRIMARY KEY (orderdetailid);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (orderid);


--
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (paymentid);


--
-- Name: reservations reservations_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (reservationid);


--
-- Name: schedules schedules_employeeid_shifttime_key; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_employeeid_shifttime_key UNIQUE (employeeid, shifttime);


--
-- Name: schedules schedules_pkey; Type: CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_pkey PRIMARY KEY (scheduleid);


--
-- Name: orderdetails orderdetails_itemid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_itemid_fkey FOREIGN KEY (itemid) REFERENCES public.menuitems(itemid) ON DELETE SET NULL;


--
-- Name: orderdetails orderdetails_orderid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.orderdetails
    ADD CONSTRAINT orderdetails_orderid_fkey FOREIGN KEY (orderid) REFERENCES public.orders(orderid) ON DELETE CASCADE;


--
-- Name: payments payments_orderid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_orderid_fkey FOREIGN KEY (orderid) REFERENCES public.orders(orderid) ON DELETE SET NULL;


--
-- Name: schedules schedules_employeeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jamesbiser
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_employeeid_fkey FOREIGN KEY (employeeid) REFERENCES public.employees(employeeid) ON DELETE CASCADE;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: jamesbiser
--

GRANT USAGE ON SCHEMA public TO bootstrap_user;


--
-- Name: TABLE customers; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.customers TO bootstrap_user;


--
-- Name: SEQUENCE customers_customerid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.customers_customerid_seq TO bootstrap_user;


--
-- Name: TABLE employees; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.employees TO bootstrap_user;


--
-- Name: SEQUENCE employees_employeeid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.employees_employeeid_seq TO bootstrap_user;


--
-- Name: TABLE menuitems; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.menuitems TO bootstrap_user;


--
-- Name: SEQUENCE menuitems_itemid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.menuitems_itemid_seq TO bootstrap_user;


--
-- Name: TABLE orderdetails; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.orderdetails TO bootstrap_user;


--
-- Name: SEQUENCE orderdetails_orderdetailid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.orderdetails_orderdetailid_seq TO bootstrap_user;


--
-- Name: TABLE orders; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.orders TO bootstrap_user;


--
-- Name: SEQUENCE orders_orderid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.orders_orderid_seq TO bootstrap_user;


--
-- Name: TABLE payments; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.payments TO bootstrap_user;


--
-- Name: SEQUENCE payments_paymentid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.payments_paymentid_seq TO bootstrap_user;


--
-- Name: TABLE reservations; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.reservations TO bootstrap_user;


--
-- Name: SEQUENCE reservations_reservationid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.reservations_reservationid_seq TO bootstrap_user;


--
-- Name: TABLE schedules; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.schedules TO bootstrap_user;


--
-- Name: SEQUENCE schedules_scheduleid_seq; Type: ACL; Schema: public; Owner: jamesbiser
--

GRANT SELECT,USAGE ON SEQUENCE public.schedules_scheduleid_seq TO bootstrap_user;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: jamesbiser
--

ALTER DEFAULT PRIVILEGES FOR ROLE jamesbiser IN SCHEMA public GRANT SELECT,INSERT,DELETE,UPDATE ON TABLES  TO bootstrap_user;


--
-- PostgreSQL database dump complete
--

