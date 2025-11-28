create table ad_space (
  id bigserial primary key,
  name text not null,
  type varchar(32) not null,
  city text not null,
  address text not null,
  price_per_day numeric(12,2) not null,
  status varchar(32) not null,
  created_at timestamptz not null,
  updated_at timestamptz not null
);

create index idx_ad_space_status on ad_space(status);
create index idx_ad_space_city on ad_space(city);
create index idx_ad_space_type on ad_space(type);

create table booking_request (
  id bigserial primary key,
  ad_space_id bigint not null references ad_space(id),
  advertiser_name text not null,
  advertiser_email text not null,
  start_date date not null,
  end_date date not null,
  status varchar(32) not null,
  total_cost numeric(12,2) not null,
  created_at timestamptz not null,
  updated_at timestamptz not null
);

create index idx_booking_status on booking_request(status);
create index idx_booking_space_dates on booking_request(ad_space_id, start_date, end_date);
