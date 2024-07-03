TRUNCATE TABLE sample;

INSERT INTO public.sample (id, name, description, process_status, version, created_date, last_modified_date)
VALUES ('e7f936b5-3fca-49d4-800d-6b16a7911c1d', 'Test Sample', 'Solid sample', 'ACCEPTED'::sample_process_status, 0,
        '2024-06-26 00:00:00.000000', '2024-06-26 00:00:00.000000'),
       ('63266b18-fe31-4e41-a409-208e9ae1d643', 'Test Sample', 'unstable sample', 'ACCEPTED'::sample_process_status, 0,
        '2024-06-26 00:00:00.000000', '2024-06-26 00:00:00.000000'),
       ('f6049dc9-08fe-490c-8bb2-3d8643c6edb4', 'Sample Processed', 'Solid sample', 'PROCESSED'::sample_process_status, 0,
        '2024-06-26 00:00:00.000000', '2024-06-26 00:00:00.000000');