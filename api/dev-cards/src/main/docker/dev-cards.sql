--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2024-08-30 13:38:30

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
-- TOC entry 215 (class 1259 OID 119201)
-- Name: task_entity_task_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_entity_task_tags (
    task_entity_task_id bigint NOT NULL,
    task_tags character varying(255)
);


ALTER TABLE public.task_entity_task_tags OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 119193)
-- Name: task_updates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_updates (
    task_id bigint NOT NULL,
    created_at timestamp(6) with time zone,
    creator_user_id bigint,
    creator_username character varying(255),
    description character varying(2000),
    editors_id bigint[],
    editors_usernames character varying(255)[],
    old_descriptions character varying(255)[],
    update_id bigint,
    updated_at timestamp(6) with time zone
);


ALTER TABLE public.task_updates OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 133821)
-- Name: task_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_user (
    user_id bigint NOT NULL,
    task_id bigint NOT NULL
);


ALTER TABLE public.task_user OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 119205)
-- Name: tasks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks (
    task_id bigint NOT NULL,
    color character varying(255),
    created_at timestamp(6) without time zone,
    description text,
    effort character varying(255),
    priority character varying(255),
    progress character varying(255),
    soft_delete boolean NOT NULL,
    status character varying(255),
    subtitle character varying(255),
    task_type character varying(255),
    title character varying(255),
    updated_at timestamp(6) without time zone,
    user_id bigint,
    owner_id bigint,
    workspace_id bigint,
    CONSTRAINT tasks_color_check CHECK (((color)::text = ANY (ARRAY[('RED'::character varying)::text, ('BLUE'::character varying)::text, ('GREEN'::character varying)::text, ('ORANGE'::character varying)::text, ('YELLOW'::character varying)::text, ('PURPLE'::character varying)::text, ('BROWN'::character varying)::text, ('GRAY'::character varying)::text, ('BLACK'::character varying)::text, ('PINK'::character varying)::text, ('CYAN'::character varying)::text, ('VIOLET'::character varying)::text, ('INDIGO'::character varying)::text, ('FUCHSIA'::character varying)::text, ('ROSE'::character varying)::text, ('SKY'::character varying)::text, ('TEAL'::character varying)::text, ('EMERALD'::character varying)::text, ('LIME'::character varying)::text, ('AMBER'::character varying)::text, ('SLATE'::character varying)::text]))),
    CONSTRAINT tasks_effort_check CHECK (((effort)::text = ANY ((ARRAY['LOW'::character varying, 'MEDIUM'::character varying, 'HIGH'::character varying])::text[]))),
    CONSTRAINT tasks_priority_check CHECK (((priority)::text = ANY ((ARRAY['VERY_LOW'::character varying, 'LOW'::character varying, 'MEDIUM'::character varying, 'HIGH'::character varying, 'VERY_HIGH'::character varying])::text[]))),
    CONSTRAINT tasks_progress_check CHECK (((progress)::text = ANY ((ARRAY['NULL'::character varying, 'NOT_FUNCTIONAL'::character varying, 'BASIC'::character varying, 'INTERMEDIATE'::character varying, 'ADVANCE'::character varying])::text[]))),
    CONSTRAINT tasks_status_check CHECK (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('PROGRESS'::character varying)::text, ('TESTING'::character varying)::text, ('COMPLETED'::character varying)::text, ('BLOCKED'::character varying)::text]))),
    CONSTRAINT tasks_task_type_check CHECK (((task_type)::text = ANY ((ARRAY['CODE'::character varying, 'ART'::character varying, 'DOCUMENTATION'::character varying, 'BUG'::character varying, 'TESTING'::character varying, 'MARKETING'::character varying])::text[])))
);


ALTER TABLE public.tasks OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 119218)
-- Name: tasks_dependencies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks_dependencies (
    task_entity_task_id bigint NOT NULL,
    dependencies_task_id bigint NOT NULL
);


ALTER TABLE public.tasks_dependencies OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 119221)
-- Name: tasks_progress_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks_progress_items (
    task_id bigint NOT NULL,
    is_completed boolean,
    sentence character varying(1000),
    issue_id bigint NOT NULL
);


ALTER TABLE public.tasks_progress_items OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 119204)
-- Name: tasks_task_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.tasks ALTER COLUMN task_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tasks_task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 119224)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    roles character varying(255),
    CONSTRAINT user_roles_roles_check CHECK (((roles)::text = ANY ((ARRAY['ROLE_ADMIN'::character varying, 'ROLE_OWNER'::character varying, 'ROLE_MODERATOR'::character varying, 'ROLE_USER'::character varying])::text[])))
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 119229)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id bigint NOT NULL,
    about text,
    avatar character varying(255),
    created_at timestamp(6) without time zone,
    email character varying(255),
    github_profile text,
    password character varying(255),
    soft_delete boolean NOT NULL,
    updated_at timestamp(6) without time zone,
    username character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 119236)
-- Name: users_designated_tasks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_designated_tasks (
    user_entity_user_id bigint NOT NULL,
    designated_tasks_task_id bigint NOT NULL
);


ALTER TABLE public.users_designated_tasks OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 119228)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN user_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 119239)
-- Name: users_workspaces; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_workspaces (
    user_id bigint NOT NULL,
    workspace_id bigint NOT NULL
);


ALTER TABLE public.users_workspaces OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 142155)
-- Name: workspace_collaborators; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.workspace_collaborators (
    workspace_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.workspace_collaborators OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 119242)
-- Name: workspace_moderators; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.workspace_moderators (
    workspace_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.workspace_moderators OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 119245)
-- Name: workspace_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.workspace_users (
    workspace_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.workspace_users OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 119249)
-- Name: workspaces; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.workspaces (
    workspace_id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    description text,
    project_avatar text,
    project_name character varying(255),
    soft_delete boolean NOT NULL,
    updated_at timestamp(6) without time zone,
    user_id bigint
);


ALTER TABLE public.workspaces OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 119248)
-- Name: workspaces_workspace_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.workspaces ALTER COLUMN workspace_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.workspaces_workspace_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3409 (class 0 OID 119201)
-- Dependencies: 215
-- Data for Name: task_entity_task_tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_entity_task_tags (task_entity_task_id, task_tags) FROM stdin;
27	client
27	ui
27	card
50	client
50	ui
50	backend
50	authorization
50	management
42	release
42	deploy
44	ui/ux
44	client
44	theme
44	custom
47	bug
81	bug
81	ui
47	ui
47	client
67	ui
67	bug
81	tags
55	bug
55	jwt
55	auth
55	client
55	api
51	ui
51	client
36	ux
34	test
34	bug
34	ui
34	modal
36	custom
36	client
15	client
15	ui
15	data visualization
70	ui
70	client
70	view
18	backend
18	api
18	security
18	jwt
18	authorization
17	backend
17	api
17	security
17	auth
37	state
68	ui
68	client
68	view
68	profile
68	control
37	api
37	json
37	backup
41	ui
41	herencia
41	task
73	ui
73	btn
73	client
32	ux/ui
32	web
32	custom
32	client
69	bug
69	ui
69	tags
21	core
\.


--
-- TOC entry 3408 (class 0 OID 119193)
-- Dependencies: 214
-- Data for Name: task_updates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_updates (task_id, created_at, creator_user_id, creator_username, description, editors_id, editors_usernames, old_descriptions, update_id, updated_at) FROM stdin;
16	2024-08-08 01:54:08.269983-03	1	admin	BLOCKED porque ya no se va a usar la api para actualizar campos.	{}	{}	{}	467369328959767316	2024-08-08 01:54:08.269983-03
34	2024-08-08 11:39:25.36258-03	1	admin	fixed New Task modal reopen	{}	{}	{}	783057948711470081	2024-08-08 11:39:25.36258-03
34	2024-08-08 11:41:43.261339-03	1	admin	Fixed new issue modal reopen	{}	{}	{}	1698764791442718738	2024-08-08 11:41:43.261339-03
35	2024-08-08 13:13:01.661296-03	1	admin	Agregado un watcher que actualice no solo el color sino el progress y el color de la prioridad. 	{}	{}	{}	4634685680412478316	2024-08-08 13:13:01.661296-03
43	2024-08-08 13:29:23.891685-03	1	admin	cambiado el layout del menu -para evitar que 'all tasks' se abra al costado, comportamiento heredao de daisy ui-, agregado un overflow hide. Fixeado	{}	{}	{}	5752183824780970204	2024-08-08 13:29:23.891685-03
39	2024-08-08 14:03:35.246527-03	1	admin	Removida la restricción en la base de datos, cambiado el código de la API para evitar limitaciones de dependencias. 	{}	{}	{}	1256130127362803788	2024-08-08 14:03:35.246527-03
38	2024-08-08 15:18:26.664727-03	1	admin	fixed! Los usuarios pueden crear todos los workspaces q quieran!	{}	{}	{}	4900391036257191159	2024-08-08 15:18:26.664727-03
44	2024-08-08 19:47:49.084157-03	1	admin	Agregada primera implementación. Hay que ir testeando los distintos themes y los colores porque hay lugares donde se hace dificil la lectura con los colores actuales. En definitiva, ya está en funcionamiento.	{}	{}	{}	2456326502963430317	2024-08-08 19:47:49.084157-03
42	2024-08-08 19:45:00.259531-03	1	admin	Agregada funcionalidad para crear nuevos workspaces. Además se agregó un "theme selector".	{}	{}	{}	1752155513322294123	2024-08-08 19:45:00.259531-03
42	2024-08-09 18:22:46.810821-03	1	admin	https://medium.com/spring-boot/free-hosting-bliss-deploying-your-spring-boot-app-on-render-d0ebd9713b9d	{}	{}	{}	7259116880953492129	2024-08-09 18:22:46.810821-03
41	2024-08-09 20:44:11.043045-03	1	admin	Agregado una versión mínima con navegación a las tareas padre.	{}	{}	{}	551547365030711281	2024-08-09 20:44:11.043045-03
46	2024-08-12 20:11:32.374893-03	1	admin	Fixed! Desde el componente TaskMiniCard se chequea la id del current project en el project store y si es distinta a la de la tarea, se cambia la información del workspace por la correcta.	{}	{}	{}	4192281014869508556	2024-08-12 20:11:32.374893-03
18	2024-08-14 11:01:28.257069-03	1	admin	Cada workspace tiene que tener sus propios roles de usuarios. Ya estoy investigando cómo hacer e insertar los roles de los distintos workspaces en el JWT	{}	{}	{}	31436811174135	2024-08-14 11:01:28.257069-03
18	2024-08-14 21:30:02.122044-03	1	admin	Realizada mi propia implementación evitando la "magia" de Spring en la clase jwtUtils	{}	{}	{}	58306444168391	2024-08-14 21:30:02.122044-03
47	2024-08-15 17:23:38.095331-03	1	admin	Arreglado el comportamiento raro y mejorado las transiciones	{}	{}	{}	20397807534972	2024-08-15 17:23:38.095331-03
50	2024-08-19 20:45:10.203074-03	1	admin	Se puede invitar usuarios, agregar/remover mods, pero falta la habilidad para remover usuarios. Los controles están filtrados según nivel de autorización. 	{}	{}	{}	51032608371881	2024-08-19 20:45:10.203074-03
50	2024-08-21 10:19:01.334-03	1	admin	el sistema de flags va a tener q  esperar hasta después de 0.0.1	{}	{}	{}	37993720605452	2024-08-21 10:19:01.334-03
50	2024-08-21 12:33:59.52-03	1	admin	Project settings ahora es un componente modular para agregar o remover fácilmente opciones nuevas de configuración	{}	{}	{}	34648801060593	2024-08-21 12:33:59.52-03
69	2024-08-22 12:09:52.548-03	1	admin	todavía falta la opción para remover tags	{}	{}	{}	47973825826741	2024-08-22 12:09:52.548-03
15	2024-08-06 15:29:34.63527-03	1	admin	Añadida la vista con el layout principal. Falta el drag&drop de las cards entre columnas. \n\nTambién pienso agregar un botón para cambiar el tamaño de las tarjetas (entre mini y micro).	{}	{}	{}	6983390870742583307	2024-08-06 15:29:34.63527-03
15	2024-08-07 21:02:56.850496-03	1	admin	descartada la implementación de drag and drop por ahora. El grado de complejidad para actualizar task no vale la pena para detener el desarrollo de la app.	{}	{}	{}	7519127868278719979	2024-08-07 21:02:56.850496-03
15	2024-08-22 21:48:42.431-03	1	admin	No se va a continuar con la implementación de las columnas o el listado de tareas de scrum en la barra de tarea lateral. Y los filtros necesitan la implementación de custom configs. Puede ser reabierta o continuada en tareas hijas.	{}	{}	{}	89775835997709	2024-08-22 21:48:42.431-03
15	2024-08-22 21:50:00.276-03	1	admin	Lo mismo pasa con el drag and drop. Hasta que no esté el refactor para trabajar local no tiene sentido (aunque ya pensé algunas soluciones, como por ej, asignar un usuario a una tarea cambia del backlog a in progress. 	{}	{}	{}	39761920883065	2024-08-22 21:50:00.276-03
73	2024-08-26 14:59:17.283-03	1	admin	Se agregó un rol nuevo (ROLE_COLLABORATOR) para diferenciar entre usuarios comunes y los q pueden autoasignarse tareas.	{}	{}	{}	35255791935811	2024-08-26 14:59:17.283-03
27	2024-08-26 18:34:02.569-03	1	admin	flag system será agregado cuando se profundice el sistema de tags y se agreguen las custom configs (los flags van a estar guardados en los settings del proyecto).	{}	{}	{}	48603005418278	2024-08-26 18:34:02.569-03
51	2024-08-16 19:18:39.27958-03	1	admin	Agregados los componentes de filtro por texto y de botón para cambiar tamaño. Agregados al componente AllTaskView con funcionalidad plena. Faltarían más filtros pero de eso depende que cree el glosario.	{}	{}	{}	17402199996965	2024-08-16 19:18:39.27958-03
51	2024-08-19 11:39:08.887051-03	1	admin	Agregué Glosarios Customs como dependencia porque van a ser necesarios para crear más filtros	{}	{}	{}	61879131757870	2024-08-19 11:39:08.887051-03
51	2024-08-23 23:07:04.047-03	1	admin	Agregados filtros para las props principales de Task.	{}	{}	{}	43646255053714	2024-08-23 23:07:04.047-03
51	2024-08-26 20:35:26.884-03	1	admin	Agregué el componente TaskListFilters que tiene el text input más los botones para filtrar por props, para ser reutilizado (por ahora en blocked tasks)	{}	{}	{}	9373435008596	2024-08-26 20:35:26.884-03
68	2024-08-27 13:17:56.437-03	1	admin	Para completar el perfil de usuario con datos que no son actualizados cuando se crea el usuario, habría q crear un componente para la configuración de la app en general y también para editar el perfil de usuario	{}	{}	{}	53240831996605	2024-08-27 13:17:56.437-03
77	2024-08-27 16:45:30.908-03	1	admin	Añadido un 'BaseToggle' reusable y diferencié entre 'darker cards' y 'darker minicards' para poder cambiar el estilo entre TheTask y MiniCard.	{}	{}	{}	22011853964452	2024-08-27 16:45:30.908-03
66	2024-08-29 17:31:51.176-03	1	admin	Integré TagNavigationPanel a TaskFIlterList y se maneja toda la lógica de filtrado en este componente. AllTaskView solo manda tasks y las recibe filtradas.	{}	{}	{}	34062712409378	2024-08-29 17:31:51.176-03
\.


--
-- TOC entry 3423 (class 0 OID 133821)
-- Dependencies: 229
-- Data for Name: task_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_user (user_id, task_id) FROM stdin;
1	54
1	27
1	43
1	79
1	68
1	26
1	44
1	51
1	35
1	47
1	67
1	15
1	34
1	38
1	50
1	18
1	39
1	55
1	62
1	41
1	61
1	80
1	46
1	23
1	20
1	22
1	73
1	77
1	81
1	66
1	21
1	82
1	69
1	65
1	74
1	76
\.


--
-- TOC entry 3411 (class 0 OID 119205)
-- Dependencies: 217
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tasks (task_id, color, created_at, description, effort, priority, progress, soft_delete, status, subtitle, task_type, title, updated_at, user_id, owner_id, workspace_id) FROM stdin;
3	GREEN	2024-07-17 22:43:43.635034	asdasd	MEDIUM	LOW	NULL	t	PENDING	asdasd	ART	asdasd	2024-07-17 22:43:43.635034	\N	1	1
2	PURPLE	2024-07-17 22:30:38.850813	adasdasa asfa safa askaf  mafakjs nfans kñañk naksñfdsa a fadsfadf asas das 	LOW	VERY_LOW	NULL	t	PENDING	example2	MARKETING	task2	2024-07-17 22:30:38.850813	\N	1	1
4	RED	2024-07-18 12:14:27.676214	asdas	HIGH	VERY_HIGH	NULL	t	PENDING	fix me please	BUG	FIX ME	2024-07-18 12:14:27.676214	\N	1	1
6	INDIGO	2024-07-18 12:27:26.196208	asdasd	MEDIUM	HIGH	NULL	t	PENDING	asdasd	ART	Sprite art	2024-07-18 12:27:26.196208	\N	1	1
9	PINK	2024-07-19 12:07:34.749051	this is another test jejejejejejje	MEDIUM	VERY_HIGH	NULL	t	PENDING	this is another test	TESTING	another test	2024-07-19 12:07:34.749051	\N	1	1
8	LIME	2024-07-19 11:40:55.461379	this is an example with long enums string to test the ui	MEDIUM	VERY_HIGH	NULL	t	PENDING	nothing to see here	DOCUMENTATION	Long title test example	2024-07-19 11:40:55.461379	\N	1	1
1	GREEN	2024-07-17 22:30:12.913682	adasdas	HIGH	MEDIUM	NULL	t	PENDING	example	ART	task	2024-07-17 22:30:12.913682	\N	1	1
31	INDIGO	2024-08-07 13:23:10.767158	asdasd	MEDIUM	VERY_HIGH	NULL	t	PENDING	asda	CODE	another	2024-08-07 13:23:10.767158	\N	1	1
54	INDIGO	2024-08-17 01:35:16.071612	Tarea para mantener un seguimiento del curso de go 	HIGH	MEDIUM	NULL	f	PROGRESS	Video Tracker	CODE	CURSO | GO ZTM	2024-08-28 01:19:17.155403	\N	1	15
7	YELLOW	2024-07-18 14:35:34.497761	Crear una vista de lista de tasks con varias columnas: ice-box, urgent, progress, testing, complete.	HIGH	MEDIUM	NULL	t	PENDING	Componente principal para desarrollo	CODE	Scrum view	2024-07-18 14:35:34.497761	\N	1	1
12	BLUE	2024-07-23 18:33:56.030023	Polish security with role's authorization system. 	LOW	MEDIUM	NULL	t	PENDING	Add authorization flow with roles	CODE	Securitty/Authorization	2024-07-23 18:33:56.030023	\N	1	1
10	FUCHSIA	2024-07-19 19:34:50.892536	Control para mods y owners de proyectos. 	MEDIUM	MEDIUM	NULL	t	PENDING	Similar to the task control side menu.	CODE	Project Side Menu	2024-07-25 20:00:08.551398	\N	1	1
11	FUCHSIA	2024-07-23 18:28:35.408201	Basic Oauth2 with google implementation. Need some hard work with Spring internal 'magic'"	MEDIUM	LOW	NOT_FUNCTIONAL	t	PENDING	Login with Google OAuth2	CODE	OAuth2 Google	2024-07-25 20:01:37.798386	\N	1	1
13	AMBER	2024-07-23 20:52:09.773243	Now the client has to do a for loop with multiple api call to add arrays to different taks's props. It would be useful add the ability to add/remove arrays with a single api call.	LOW	VERY_LOW	NULL	t	PENDING	To Add/remove tags, users, dependencies	DOCUMENTATION	Add Array methods/endpoints	2024-07-25 19:58:10.776895	\N	1	1
27	PURPLE	2024-08-06 15:32:11.347053	A través de un boolean enviado por props cambiar la visualización del componente "TaskMiniCard" 	LOW	HIGH	ADVANCE	f	COMPLETED	Una variante de la visualización de la task más pequeño	CODE	MicroTask View	2024-08-28 23:55:09.178153	\N	1	1
63	AMBER	2024-08-21 12:57:09.753585	asd	MEDIUM	MEDIUM	NULL	t	PENDING	exampleee	TESTING	example	2024-08-21 12:57:47.134015	\N	3	1
43	RED	2024-08-08 01:49:01.619973	Chequear el overflow dle menu para q oculte lo q no se ve y se vean barras laterales en todo caso en lugar de abirrse más al costado. De todos modos el "ALL" de tasks debería ser una  opciíón tipo filtor para verlas todas las tasks y buscarlas.  En el menu lateral deberían verse solo designadas en progreso o testing y quizas las tasks con mas alta prioridad (y también las que tengan updates)	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	Se rompe el menu cuando hay muchas tareas y está abierto el tick	BUG	BUG UI Project Lateral Menu overflow	2024-08-28 01:19:17.156404	\N	1	1
79	PURPLE	2024-08-26 15:08:18.495934	La versión móvil puede esperar, no así la versión para notebook.	MEDIUM	VERY_HIGH	NULL	f	PROGRESS	Al menos para pantallas chicas tipo notebook	CODE	UI | Responsive design	2024-08-28 01:19:17.156404	\N	1	1
49	YELLOW	2024-08-14 21:58:03.275996	asdasd	LOW	VERY_HIGH	NULL	t	PENDING	eaaaxas	CODE	example	2024-08-14 22:39:12.775736	\N	1	1
75	PURPLE	2024-08-22 21:01:17.301564	Las tasks updates como formato se pueden usar también para agregar "comentarios" de usuarios en distintas partes de la app	MEDIUM	LOW	NULL	f	PENDING	Crear un componente para las tasks updates 	CODE	UI | Task update component	2024-08-28 01:19:17.157402	\N	1	1
78	INDIGO	2024-08-26 10:26:44.981279	 página web:\n* textos alinear a la izquierda \nen "preguntas frecuentes" \n* sacar la línea roja que divide con el título "¿por que elegir tto online?\n* cambiar diseño del formulario para contacto: bordes cuadrados de todo lo que tenga borde y sacar líneas naranjas. "Enviar" tiene tilde en la i\n*\n[11:11 a.m., 16/8/2024] Vane: el fondo donde dice Leonardo....cambiar de color por el mismo fondo que contacto	LOW	HIGH	NULL	f	PENDING	Cambios propuestos por Vanesa	CODE	UI | Cambios	2024-08-28 01:19:17.171405	\N	1	21
28	GRAY	2024-08-06 15:38:01.284725	asdasdas	MEDIUM	VERY_LOW	NULL	t	PENDING	eadas	BUG	ex2	2024-08-06 15:38:01.284725	\N	1	1
19	PINK	2024-08-01 19:50:41.993682	Lorem ipsum odor amet, consectetuer adipiscing elit. Maximus suscipit libero facilisi tristique mus non. Aliquet etiam ac platea eleifend augue luctus felis turpis. Nec in conubia libero habitasse bibendum donec sapien pretium. Lectus facilisi tincidunt interdum risus commodo platea gravida. Orci aliquet cursus morbi nascetur dignissim inceptos id? Ut mattis suscipit diam suspendisse dictum condimentum. Fermentum placerat finibus eleifend libero; potenti litora aptent. Id nisi non parturient quam vestibulum velit curae.\n\nDolor morbi tempor facilisis ex risus nullam tristique. Eros imperdiet convallis phasellus tempus vestibulum fermentum class. Ipsum vulputate interdum sodales nostra quam viverra. Lorem feugiat pellentesque dis ipsum adipiscing hac nulla. Sociosqu in vel sit lacus pharetra suspendisse. Curabitur litora ultricies suscipit vehicula praesent interdum cubilia vulputate gravida. Maecenas velit praesent taciti fringilla libero est mus duis. Ante curae sociosqu feugiat in, efficitur proin per mauris. Erat justo porttitor tempor penatibus malesuada per porttitor. Curabitur sodales habitant tortor nisi condimentum et elit.\n\nTurpis curae in ex tellus primis blandit. Faucibus sagittis taciti in mollis a nisi pharetra. Dictum dictumst hendrerit finibus himenaeos senectus risus curabitur lacinia semper. Magnis mus enim diam aptent cras facilisis rhoncus non. Aptent tempor per natoque purus lobortis per nec dictum inceptos? Feugiat cras sollicitudin; nam praesent fringilla amet. Porta auctor faucibus sit, condimentum laoreet tristique interdum curabitur.\n\nClass pellentesque amet luctus viverra, inceptos lacinia. Aliquam accumsan odio habitasse facilisis habitasse id. Maximus imperdiet curabitur hendrerit vulputate ridiculus. Purus morbi hendrerit volutpat; erat senectus velit interdum. Eu sit purus nullam erat dictumst at vivamus litora sollicitudin. Platea enim faucibus, euismod primis nec primis. Cras potenti duis eleifend libero magnis montes porta.\n\nPurus magna fermentum nascetur ac sit integer condimentum. Felis semper ut mattis adipiscing libero ridiculus dignissim. Ante tempor interdum venenatis amet porta nulla euismod congue. Luctus vitae mi consectetur ullamcorper curae molestie tellus ad. Lobortis feugiat urna nam bibendum blandit nisl ex. Phasellus volutpat quisque fames varius proin mus sapien. Semper pretium turpis congue adipiscing pharetra. Primis sed vel dapibus vitae risus imperdiet proin. Porttitor proin at nec molestie est risus. Laoreet aliquet proin purus dapibus, nullam ultricies quisque.	HIGH	VERY_HIGH	BASIC	t	TESTING	this is just an example	CODE	Example	2024-08-08 10:33:20.797291	\N	1	1
68	PURPLE	2024-08-22 01:10:05.138734	Con un tag de la descripción del usuario se puede tener una referencia a un objeto de la configuración del workspace donde puede haber data del usuario pero de dicho workspace, como por ejemplo, qué rol ocupa, en qué grupo está, tiempo en la plataforma, cantidad de tareas creadas, etc. 	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	Los usuarios son simplemente nicknames. Necesitan más vida y funcionalidades.	CODE	UI | Crear vista de perfil de usuario o semejante	2024-08-29 18:00:02.326069	\N	1	1
26	FUCHSIA	2024-08-03 12:27:33.926169	El protagonista se despierta de un sueño criogenizado y se encuentra con una situación distinta cada vez. Meses, años, décadas pasaron.\n\nQuizás el layout de la nave puede variar ligeramente y parte de las instalaciones varían según las condiciones de la situación. El escenario puede variar de vez en vez. La nave, las habitaciones, el lugar en el espacio, pero también pueden ser pequeñas colonias mineras en lunas, campos de asteroides, o en, lo más ambicioso, planetas. \n\nLas "situaciones" pueden ser tratadas como episodios, unas reglas que definen el escenario actual. Por ej, hay una IA dentro de un humano escondida y dormida y tiene una ligera chance de despertar, de hacerlo puede ser amistosa o ir de a poco replicándose y tomando la nave. Otra variante pueden ser zombies! peleas de facciones, orbitando un planeta, infiltrados por aliens, etc.\n\nSegún el tipo de episodio, se generan ciertos buff en los npcs y según los moods/estados/buff/debuff que tenga -modificados por las distintas situaciones cotidianas, llegar a cierto nivel de un estado desencadenará acciones, como las crisis nerviosas o ataques psicóticos de rimworld, pero necesariamente ese tipo. Por ejemplo: ante la falta de alimento y hambre extremo un npc puede salir a pedir, a buscar más alimento, si hablo con otros npcs de su problema capaz busque organizarse, \n\nEl despertar del protagonista está asociada a resolver un problema policial-organizativo. Siempre es un grupo de personas (gobierno, disidencia, ias, ¿aliens?, ¿vampiros?, el simple pueblo organizado, o un [familiar/amante/enemigo/amigo]_del pasado (q también puede estar criogenizado o no). La realidad es que de alguna forma u otra hay un misterio a resolver asociado al despertar, incluso la versión más mundana -se necesitan mineros-. Incluso si lo despiertan para "poblar un planeta". \n\nHay otras naves generacionales/facciones que de alguna forma compiten. Nada muy complejo a nivel emulación, historias más lineales. Los npcs son más básicos y ganan funcionalidad sólo si interaccionan directamente con el personaje. El personaje los "bautiza" al preguntarle los nombres e interactuar más. Incluso esto puede ser una forma de optimizar el funcionamiento de la nave generacional del protagonista. En lugar de generar todo para todos, tener un control mínimo y solo darle más jugo si el jugador se involucra en la situación. Cada npc tiene algunos traits base donde se pueden desarrollar otros traits.	HIGH	VERY_HIGH	NULL	f	PROGRESS	Documento para pensar mecánicas, ideas, sistemas, etc.	DOCUMENTATION	Ideas de diseño	2024-08-29 01:38:13.982484	\N	1	3
64	AMBER	2024-08-21 13:34:18.529039	ASDASD	MEDIUM	LOW	NULL	t	PENDING	EX	CODE	EXAMPLE23	2024-08-21 13:34:18.529039	\N	1	1
44	PURPLE	2024-08-08 10:22:01.211198	Agregar la posibilidad de cambiar el tema de colores desde la ui	MEDIUM	VERY_LOW	ADVANCE	f	COMPLETED	Elemento para cambiar entre distintos themes de daisyui	CODE	UI | Alternat themes	2024-08-28 01:19:17.158401	\N	1	1
29	YELLOW	2024-08-06 15:41:47.146354	asdasd	LOW	VERY_LOW	NULL	t	PENDING	asda	CODE	ex3	2024-08-06 15:41:47.146354	\N	1	1
51	PURPLE	2024-08-15 19:56:21.98315	Añadir buscador, boton para crear tareas /cambiar tamaño de card, y filtros según status y color	LOW	LOW	ADVANCE	f	COMPLETED	añadir filtros y  controles	CODE	UI | All Tasks view filters	2024-08-29 01:18:55.322166	\N	1	1
35	RED	2024-08-07 23:49:47.898556	Se ve que no hay un watch asociado al cambio del color de colores del componente minicard q cuando se crea una nueva task, se queda con el color viejo, es un comportamiento raro, pero lo asocio a que asocio el color a una variable reactiva q no se está actualizando en algún lugar del flujo	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	Cards muestran distintos colores del q deberían	BUG	BUG: UI: Color bar 	2024-08-28 01:19:17.159401	\N	1	1
59	BROWN	2024-08-19 23:17:04.471467	Enlace: webmail.sociales.uba.ar\nCorreo: jcobo@sociales.uba.ar\nClave: 32462025.UBA / H@cersebolita1	LOW	VERY_LOW	NULL	f	PENDING	jec 	DOCUMENTATION	fsoc _ 	2024-08-28 01:19:17.160402	\N	1	15
47	RED	2024-08-09 23:16:08.348836	Es un problema q se empezó a dar cuando forcé a aparecer completamente on load y después volver a desaparecer de la pantalla, hasta q el usuario haga hover en ella). Ya que se va a fixear, podría también trabajar en la animación y hacerlo correctamente.	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	cuando se carga la web y luego de loguearse	BUG	BUG | UI | Lateral Menu weird behaivor	2024-08-28 23:40:12.432498	\N	1	1
67	RED	2024-08-22 01:01:39.86137	mejorar la vista de la selección. Probablemente tiene q ser una vista limitada, incluso con algún tipo de paginación, pero con filtros para achicar la lista, quizás interactivos según colores, pero también con un input de texto para buscar tags o palabras clave en títulos. 	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	El width fijo del modal lo hace incontrolable cuando la lista de tareas es grande	BUG	UI | Dependency selector  overflow	2024-08-29 17:48:59.953593	\N	1	1
36	BLUE	2024-08-07 23:59:37.272108	Crear la posibilidad q el usuario haga glosarios para cada proyecto o tarea (algunas tareas -con cierto color por ej o de cierto tipo- no tienen enums asociados a desarrollo de software y quizás tengan algún modo de agregar nuevos valores al enum -tengo e xplorar las posibilidades en java-). Por ej en algún tipo de tareas los issues no tienen checkoboxes. En el tipo documentación, por ej, puede haber algun tag en la descripción q pueda ser leído para aplicar cuadros de código como en los readme de github. 	MEDIUM	MEDIUM	NULL	f	PENDING	Para todas los enums de la entidad tasks	CODE	Glosarios customs	2024-08-28 01:19:17.161418	\N	1	1
56	FUCHSIA	2024-08-17 02:31:28.615013	Propuestas de nombres para el proyecto de DevCards. Busqué Tarjeta en frances (carte) y tarea (Tâche, se pronuncia tash) y me gustó más Tàche/Tash	LOW	VERY_HIGH	NULL	f	PENDING	Elegir un nombre para el proyecto de dev-cards	MARKETING	Nombres para DevCards	2024-08-28 01:19:17.162405	\N	1	15
42	SKY	2024-08-08 00:51:15.051724	buscar hosting para el front, buscar hosting para el back, buscar hosting para la DB. La idea es tener la app en el estado actual, con alguna implementación solo para darle utilidad hasta tener el refactor y la próxima versión en un estado posible para hacer testing. \n\nAdemás tener la app en un estado para que lo puedan testear otros. Crear un par de usuarios/pass e invitarlos a un grupo con algunas notas básicas  y que testear y como reportar bugs. 	HIGH	VERY_HIGH	BASIC	f	PROGRESS	Con un refactor en mente, tener un deploy de la app actual	TESTING	DEPLOY v0.0.1	2024-08-28 23:41:09.102442	\N	1	1
37	BLUE	2024-08-08 00:22:49.218185	Sistema para guardar states de workspaces on demand en el disco, además de un autosave en el localstorage y uno más esporádico en el server (uno cada 30 min de uso?)\n\nEl refactor está principalmente acá. En la versión actual, cada actualización de la UI está basada en llamadas a la API. Ahora usar el store y el local storage y cada X cantidad de tiempo un save al server, La idea es tener la menor dependencia remota posible 	MEDIUM	HIGH	NOT_FUNCTIONAL	f	PENDING	Sistema para savear workspaces como archivo local y autosaves en localstorage	CODE	SAVE/LOAD state local	2024-08-29 17:10:06.331171	\N	1	1
34	RED	2024-08-07 23:47:11.185665	Cuando se cierran ciertos modales sin esperar q se termine la animación, se termina reabriendo el modal por el código asíncrono. 	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	modal-reopen	BUG	BUG: UX/UI Modal reopen	2024-08-29 17:56:43.381633	\N	1	1
15	PURPLE	2024-08-01 18:53:20.421723	This is one of the the tasks view in project/workspace areas. 	MEDIUM	MEDIUM	ADVANCE	f	COMPLETED	Organization / List / view	CODE	Scrum View	2024-08-29 20:17:48.358617	\N	1	1
14	PURPLE	2024-07-24 16:14:32.843854	Lorem ipsum odor amet, consectetuer adipiscing elit. Montes ex mi leo nisl nam bibendum. Dis libero ex platea pellentesque morbi senectus viverra? Metus donec montes id primis rutrum congue. Lacus eleifend at; proin netus molestie pulvinar diam. Massa dapibus commodo viverra netus taciti tortor adipiscing. Leo sodales maecenas senectus orci torquent. Dignissim sem facilisi bibendum feugiat feugiat primis ultrices turpis nulla. Orci aenean fames nullam nulla ad libero. Euismod nisl pellentesque conubia aenean gravida sapien porttitor fermentum.\n\nIn conubia facilisis aliquam sagittis torquent euismod convallis. Rhoncus mi ornare tristique nostra interdum pharetra nulla. Nascetur habitant ad lobortis elementum fringilla habitasse integer arcu. Ante imperdiet penatibus nam platea mauris mus aptent. Diam dictum sociosqu lacus torquent dolor sem mus eu. Nibh tempus consequat fermentum blandit nisi leo. Tempus sociosqu varius lorem aliquet sem lobortis magnis augue. Ridiculus hendrerit dictumst sit donec commodo cubilia in potenti.	MEDIUM	MEDIUM	BASIC	t	PROGRESS	this is an example	CODE	example	2024-07-25 20:11:14.356317	\N	1	1
70	PURPLE	2024-08-22 10:22:15.647899	La app necesita una manera rápida de acceder a las tareas con mayor prioridad. Se puede reutilizar el componente de búsqueda para filtrar por urgencia pero estaría bueno distinguir entre tareas con prioridad muy alta sin usuario designado, tareas de alta prioridad ya empezadas, tareas de alta prioridad en testing y tareas de alta prioridad terminadas. 	LOW	VERY_LOW	NULL	f	PENDING	Una view que sólo muestre tareas con top priority 	CODE	UI | Top Priority View	2024-08-28 01:19:17.162405	\N	1	1
53	GREEN	2024-08-16 18:39:43.725808	En esta entidad se van a definir las vistas que puede agregar o no el usuario. La idea es que la "All tasks view" o la "Scrum board view" sean este tipo de entidad. 	LOW	LOW	NULL	f	PENDING	crear nueva entidad para manejar las configuraciones  personalizadas de vistas	CODE	Model | View Entity	2024-08-28 01:19:17.163404	\N	1	1
45	AMBER	2024-08-08 11:38:33.236147	asdasd	MEDIUM	VERY_LOW	NULL	t	PENDING	example	TESTING	example	2024-08-08 11:38:33.236147	\N	1	1
16	ORANGE	2024-08-01 18:54:24.541687	Add more endpoints to add o remove task tags in groups (arrays)	LOW	LOW	NULL	f	BLOCKED	add/modify endpoints	CODE	add/remove array of tags	2024-08-28 01:19:17.163404	\N	1	1
38	RED	2024-08-08 00:27:47.104044	Revisar relación de entidades que solo deja una relación user-workspace	MEDIUM	VERY_HIGH	ADVANCE	f	COMPLETED	Cada usuario puede crear un sólo workspace	BUG	BUG - API -DB Limitación de creación de workspaces	2024-08-28 01:19:17.163404	\N	1	1
50	ORANGE	2024-08-15 19:47:52.576163	Agregar los controles necesarios para darle el control sobre el proyecto y la tarea a los usuarios. 	MEDIUM	HIGH	INTERMEDIATE	f	COMPLETED	Control bar para tareas asociadas al mantenimiento de un workspace	CODE	Mod/Owner controls	2024-08-28 23:41:20.083313	\N	1	1
52	GREEN	2024-08-16 18:34:47.858472	Una entidad, que va a ser una propiedad de cada workspace, donde se guarden distintas configuraciones: como ser: glosarios de colores / props como prioridad y effort, issues con o sin checkbox, además de "views" personalizadas para las tareas o "canales" q pueden ser solo de información o también de "chat".	MEDIUM	MEDIUM	NULL	f	PENDING	Crear nueva entidad para guardar las distintas configuraciones del cliente	CODE	Model | CustomConfiguration	2024-08-28 23:41:22.388539	\N	1	1
18	LIME	2024-08-01 19:04:31.673223	Added role authorization configuration for all endpoints. 	HIGH	VERY_HIGH	ADVANCE	f	COMPLETED	Security config	CODE	Role Authorization 	2024-08-28 23:40:38.962074	\N	1	1
17	YELLOW	2024-08-01 19:02:51.392226	Additional login method 	MEDIUM	VERY_LOW	NULL	f	PENDING	Second auth implementation	CODE	Oauth2	2024-08-28 23:40:43.134083	\N	1	1
71	PURPLE	2024-08-22 10:59:50.109111	Para que el modal no cambie de tamaño a lo loco, para evitar tener un modal q ocupe toda la pantalla si el número de tareas es descomunal, hay q agregarle una simple navegación (previous y next) a las tareas para seleccionarlas como dependencias.	LOW	VERY_LOW	NULL	f	PENDING	Agregar paginación a la búsqueda de dependencias	CODE	UI | Dependency selector navigation	2024-08-28 01:19:17.165419	\N	1	1
39	RED	2024-08-08 00:29:07.336133	Revisar relación entre dependencias y tasks, parece tener una limitación para llaves duplicadas. 	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	Una task solo puede ser parent de un child	BUG	BUG>API>DB Limitación de dependencias	2024-08-28 01:19:17.165419	\N	1	1
55	RED	2024-08-17 01:50:34.503728	Cuando se crea un proyecto o se agina un usuario a un proyecto, el JWT del owner o del usuario designado todavía no disponen de esa información en el JWT si es q ya estaban logueados en ese instante. Propongo dos soluciones:\n1) Para el owner q crea el proyecto y no puede crear tareas ni modificar el workspace hasta q reloguee, crear una nueva respuesta cuando se crea proyecto para incluir un nuevo token generado con la información que incluya al token.\n2) Del lado del cliente,. Cada tantos minutos en un proyecto compartido debería haber una llamada a la api para ver si hay tareas nuevas, lo que actualizaría las tareas designadas para el usuario logueado y si el cliente detecta una discrepancia, pedir un update del jwt. \n3) Para los roles hacer algo parecido, q el project lateral menu rastree discrepancias en los moderadores y si hay discrapancias si el nuevo mod es el usuario, pedir un pdate.	MEDIUM	VERY_HIGH	ADVANCE	f	COMPLETED	 Si el usuario crea un proyecto, el jwt no lo tiene incluido en UWR	BUG	BUG | JWT NEED TO GET  UPDATE EVERY FEW MINUTES	2024-08-28 23:41:26.046667	\N	1	1
60	PINK	2024-08-19 23:42:51.624266	La entidad 'task' tiene una propiedad llamada 'tags' o 'tasks_tags' que es básicamente un array de strings. Usando un simple parseo de las tags, buscando ciertos regex, en verdad, partiendo cada tag/string usando unos simbolos en particular, por ej %CSTM256; se busca el texto encerrado entre '%' y ';' y ese valor se lo guarda en un glosario en la nueva tabla de Custom configurations. Como cada workspace tendrá su propia colección de glosarios para los tags, o las distintas propiedades (por ejemplo en documentación con un tag en particular tipo se puede tratar de documentos con ideas de diseño desarrolladas o simplemente un brainstorming como este, sin necesidad de agregar un nuevo tipo, la mezcla en una task de DOCUMENTATION y un tag en particular ya puede darle al usuario un tipo customizado. Del lado del usuario se pueden usar los primeros o los últimos X cantidad de caracteres de la descripción del usuario. Por ejemplo, esto se puede usar para tagear tasks con por ejemplo '%TEAM01; tanto como perfiles de usuarios para agrupar usuarios en unas tareas especificas. Dicho sea de paso, con este sistema, se puede crear una tarea q funcione como workspace usando los issues para referir a tasks particulares, en defitniva, si un task tagueada como un workspace para un team tiene varios issues con 'tags' q en realidad tienen la id de una task por lo que una task de este tipo (creo q podría agregar el tipo MANAGEMENT) con un task %WS0005; el client sabe q se trata un tipo de tarea q se va a asociar a un tipo de vista.	MEDIUM	VERY_HIGH	NULL	f	PENDING	Cómo usar la estructura actual para aplicar una capa de customización sin agregar nuevas variables	DOCUMENTATION	Brainstorm | Customizaciones | tags	2024-08-28 21:42:22.717729	\N	1	1
62	PURPLE	2024-08-21 01:47:26.023821	Además de crear la vista de proyectos puede haber, en designated tasks, una vista de tareas designadas por proyecto. 	MEDIUM	VERY_HIGH	ADVANCE	f	TESTING	crear una vista de proyectos con tarjetas de proyectos para la página inicial de usuario	CODE	UI | Vista de proyectos para Home-Layout	2024-08-29 18:00:34.190784	\N	1	1
41	PURPLE	2024-08-08 00:42:09.548624	Poder ver a primera vista la herencia de las tasks, quizás modificar las tasks para también tener registro de los hijos. O un buscador de childs para buscar tasks child de la task actual. En definitiva es un sistema de navegación entre las distintas tasks pero también para utilizar el concepto de herencia, o de árbol, para ideas. Con la implementaciónc orrecta se puede llegar a usar la app como un fichero para cualquier usuario interesado en tener un fichero semi personalizado digital y online., con la posibilidad de compartir el trabajo con otros a través de las invitaciones. 	MEDIUM	MEDIUM	ADVANCE	f	COMPLETED	Alguna ui para poder linkear a la herencia de tasks y poder navegar entre ellas	CODE	Herencia UI	2024-08-28 01:19:17.166405	\N	1	1
72	FUCHSIA	2024-08-22 14:56:06.637522	[10:27 a.m., 16/8/2024] Vane: Holis como va? sigo buscando la fuente tipo cursiva, ya encontraré. paso algunas ideas que anote para la pag:\n[10:28 a.m., 16/8/2024] Vane: página web:\n* textos alinear a la izquierda \nen "preguntas frecuentes" \n* sacar la línea roja que divide con el título "¿por que elegir tto online?\n* cambiar diseño del formulario para contacto: bordes cuadrados de todo lo que tenga borde y sacar líneas naranjas. "Enviar" tiene tilde en la i\n*\n[11:11 a.m., 16/8/2024] Vane: el fondo donde dice Leonardo....cambiar de color por el mismo fondo que contacto	MEDIUM	VERY_HIGH	NULL	f	PENDING	feedback de vane	CODE	Vane Web	2024-08-28 01:19:17.167403	\N	1	15
61	PURPLE	2024-08-21 01:44:32.62712	Quiero q haya un feedback instantáneo cuando el usuario cambie el nombre del workspace en Proyect Settings. Quizás puede tener un mini avatar también para q se vea el cambio del avatar tambiénr eflejado ahí pero no es necesario. 	LOW	LOW	ADVANCE	f	COMPLETED	Que aparezca el nombre del proyecto en el menu lateral 	CODE	UI | PROJECT NAME IN LATERAL MENU	2024-08-28 23:34:54.732742	\N	1	1
32	BLUE	2024-08-07 15:40:17.286959	Una propiedad nueva en cada proyecto donde se asocie un color (de tareas) con un tag elegido por los usuarios, luego en las distintas vistas (por ej el scrum board) con solo mirar los colores de las tasks el usuario puede tener una idea de qué se trata. Además de agregar nuevos filtros en las distintas vistas. \nAdemás de la propiedad en la API, se debería agregar cuando se crea un nuevo usuario y el nuevo proyecto la posibilidad de asociar un par de colores a un par de tags desde el vamos. 	LOW	VERY_LOW	NULL	f	PENDING	Introducir un glosario de colores	CODE	Color Glosary	2024-08-28 23:55:30.003353	\N	1	1
80	PURPLE	2024-08-27 14:26:32.510424	Un componente donde estén las configuraciones globales de la app y la edición del perfil de usuario	MEDIUM	VERY_HIGH	ADVANCE	f	COMPLETED	Nuevo componente  para customizar experiencia de usuario y su perfil	CODE	UI | Global App Settings | Profile editor	2024-08-28 23:34:57.240649	\N	1	1
57	GREEN	2024-08-19 11:25:15.253224	asdasdsadasd	MEDIUM	LOW	NULL	t	PENDING	asdasd	CODE	asdasd	2024-08-19 11:26:38.204103	\N	1	20
30	RED	2024-08-07 13:22:18.166633	asdasd	MEDIUM	VERY_LOW	NULL	t	PENDING	asad	BUG	new example	2024-08-07 13:22:18.167632	\N	1	1
46	RED	2024-08-09 20:34:03.419627	Cuando se navega a una tarea desde el home, se usa la ruta /project/task por lo q en la barra de proyecto se ve el último proyecto ingresado en lugar del proyecto del q la tarea es parte.	MEDIUM	VERY_HIGH	ADVANCE	f	COMPLETED	Se ve el último proyecto si se navega a una task desde home	BUG	BUG | UI | Wrong project after navigation to Task	2024-08-28 23:41:17.97641	\N	1	1
23	PURPLE	2024-08-02 01:26:19.698236	sistema que maneja y distribuye tareas para los npcs. 	MEDIUM	HIGH	NULL	f	PROGRESS	Cola de tareas y metodos de cada tarea con command pattern	CODE	TaskManager	2024-08-28 01:19:17.174403	\N	2	3
20	ORANGE	2024-08-02 01:16:15.806455	Npcs con state machines, composite sprites -multiples views de vestuario e items-, command pattern para tasks,  pathfinding, moods, buff/debuff, intereses, skills, familia, biografía, etc	LOW	VERY_HIGH	NULL	f	PROGRESS	basic npc parent class	CODE	basic NPC	2024-08-28 01:19:17.174403	\N	2	3
22	PURPLE	2024-08-02 01:23:21.098725	Usando varias capas -sprites- por parte del cuerpo / item + sprites con coordenadas uv para renderizar texturas usando una look up texture. 	HIGH	HIGH	NULL	f	PROGRESS	Manejo de sprites para items, cuerpo, animaciones	CODE	Composite Sprites	2024-08-28 01:19:17.175402	\N	2	3
73	PURPLE	2024-08-22 18:44:41.112382	Un botón para asignarse una tarea a sí mismo. Habría q chequear si hace falta cambiarlo en la API, aunque podría ser un endpoint nuevo para evitar problemas con las funcionalidades de moderación.	LOW	LOW	ADVANCE	f	COMPLETED	un botón para autoasignarse una tarea	CODE	UI | 'Take task' button	2024-08-28 23:35:01.7983	\N	1	1
77	PURPLE	2024-08-23 00:47:59.599774	A veces tanto blanco hace mal a la vista y con el tema 100% oscuro veo mejor en momentos en los q tengo la vista cansada.	LOW	VERY_LOW	ADVANCE	f	COMPLETED	Dar la opción de ser un tema 100% dark 	CODE	UI | Dark theme para cards	2024-08-28 23:35:04.016178	\N	1	1
81	RED	2024-08-29 01:18:37.500875	Revisar la creación de tag, incluso, poner un buscador para mostrar un tag si es que existe, para mostrar color y no volverlo a agregar al tag pool	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	Cree la tag "tag" y ya existía	BUG	BUG | Tag creation duplicates	2024-08-29 18:00:43.597082	\N	1	1
76	PURPLE	2024-08-23 00:46:17.000546	El mismo template de project settings y el settings de la app puede ser un template customizable (el menu ya son opciones y el resto son mini vistas), por lo q puede ser que haya alguna relación con la idea propuesta de crear clases para persistir configuraciones personalizadas (vistas,templates, glosarios, estilos, etc)	MEDIUM	MEDIUM	ADVANCE	f	COMPLETED	Usando el mismo template de Project Settings	CODE	UI | Crear un Settings para la app en general	2024-08-29 18:50:25.183392	\N	1	1
40	GREEN	2024-08-08 00:37:01.398226	Implementar una suerte de DTOS para autosaves. Por ej, solo se guardan las props q cambian, el resto null, así también se pueden registrar los cambios. El cliente manda la request para savear los cambios en la api (una insane bg  ass request con todas las tasks y workspaces modificados), y se olvida de la response, sigue funcionando. Mientras el server realiza su macumba para hacer el save con sus "sub"entidades.	MEDIUM	HIGH	NULL	f	PENDING	modificar la workspace para incluir algún registro de copias de sí misma	CODE	API - Entity Workspace	2024-08-28 01:19:17.170403	\N	1	1
58	FUCHSIA	2024-08-19 11:35:24.115236	asdasd asdasd	MEDIUM	MEDIUM	NULL	t	PENDING	example	CODE	example	2024-08-19 11:35:24.115236	\N	1	16
66	PURPLE	2024-08-22 00:54:15.408746	Las Tags van a servir como base para ser más personalizable, tanto como en la ux/ui (con templates customizados de vistas de tareas, como herramientas, almanaques, incluso alarmas/timers), manejo y moderación del workspace/comunidad (tanto el agrupamiento de usuarios y tareas por grupo, así como el flag tipo reporte de usuarios de tareas con contenido polémico/prohibido, digno de ser revisado por los organizadores del workspace). Las tags, de ser posible, son el puente para complejizar las tasks sin agregar más propiedades a los modelos principales. Se probará la viabilidad de este diseño según cómo avance la próxima fase de desarrollo. Esta tarea quizás siente les bases para lo q será la versión 0.1.0 (además del trabajo 100% local) 	MEDIUM	VERY_HIGH	ADVANCE	f	COMPLETED	Los tags hoy por hoy solo sirven como algo decorativo. Necesitan funcionalidad	CODE	UX/UI Navegación por tags	2024-08-29 17:59:34.476961	\N	1	1
48	AMBER	2024-08-13 10:42:14.640963	asdasdasd	MEDIUM	VERY_LOW	NULL	t	PENDING	example2	CODE	example	2024-08-13 10:48:11.899919	\N	1	3
21	PURPLE	2024-08-02 01:18:50.539045	manejo de estados, usando command pattern y llamando a animaciones según el estado. 	MEDIUM	HIGH	NULL	f	PROGRESS	Finite State Machine, Controller de tasks y animaciones	CODE	State Machine	2024-08-28 01:19:17.175402	\N	2	3
33	AMBER	2024-08-07 23:38:22.216977	El flujo de trabajo se tiene que dar principalmente en el cliente y cuando el usuario quiera guardar su trabajo, tiene la opción de hacerlo en su disco o en la api o en los dos. \n\nLa idea es simplificar la dependencia al uso de la API para evitar el número de requests y aumentar el poder del cliente para manejar la información.\n\nSi hay autosaves en el server cada determinado tiempo pero no es el save principal significa q tengo q pensar cómo hacer varias versiones del mismo save con el  tiempo como identificador, por lo que en el momento que el usuario suba una versión del workspace al server se grabe una versión en el tiempo. Estaría pensando en una entidad distinta que guarda una versión entera de las relaciones y en la versión actual simplemente esté la última versión uplodeada. En definitiva pueden ser distintas copias de un workspace q cambian de ID. Podría haber un parseo de perfiles de usuario de datos básicos y que le avise al usuario q hay diferencias y si las quiere actualizar o conservar las del servidor. 	HIGH	HIGH	NULL	f	PENDING	Cambiar la orientación del flow de la app	CODE	Refactor	2024-08-29 15:59:43.809434	\N	1	1
82	PURPLE	2024-08-29 01:23:52.406954	El nombre de user es tan básico y lo reusé en tantos lugares (avatar circular + nick) q quizás incluso debería hacer un componente chico q incluya el link al perfil del usuario	LOW	VERY_HIGH	ADVANCE	f	TESTING	No hay una navegación de user implementada	CODE	UI | Navigation | Click en el nombre de usuario debería llevar a su perfil	2024-08-29 18:49:14.601954	\N	1	1
69	RED	2024-08-22 01:17:11.487977	El mayor bug q rompió la db fue con esta funcionalidad de la UI q está básicamente rota y para q funcione solo se tiene q agregar tags de a una. La propuesta es q cada palabra ingresada ene l input, con un enter o click, ya se haga la call a la db	LOW	VERY_HIGH	ADVANCE	f	COMPLETED	Cada vez que se agrega un tag a la lista, ya debería agregarse al server	BUG	BUG | UI agregar tags está roto.	2024-08-28 23:41:31.864488	\N	1	1
65	RED	2024-08-22 00:39:14.399754	Si se scrollea con el mouse sobre el menu, se scrollea la web del fondo. Evitar ese comportamiento. 	MEDIUM	HIGH	ADVANCE	f	COMPLETED	Una vez que el menu crece lo suficiente, no hay barra de scrolleo	BUG	BUG | UI | Project lateral menu overflow	2024-08-28 23:41:35.708267	\N	1	1
74	AMBER	2024-08-22 19:11:34.73883	Si hay un workspace abierto o con muchos usuarios y uno decide trollear y romper las bolas, con el botón de autoassign puede hacer destrozos. Por eso lo ideal es que solo usuarios 'colaboradores' puedan autoasignarse tareas. \n\nEsto va a requerir crear nuevo role en la api, un nuevo check en jwtUtils y modificar los chequeos en TaskController, además de los requerimientos en el cliente. 	MEDIUM	HIGH	ADVANCE	f	COMPLETED	Se necesita diferenciar entre cualquier usuario y usuarios colaboradores	CODE	Propuesta: Agregar un nuevo rol de usuario 	2024-08-28 23:41:12.602519	\N	1	1
\.


--
-- TOC entry 3412 (class 0 OID 119218)
-- Dependencies: 218
-- Data for Name: tasks_dependencies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tasks_dependencies (task_entity_task_id, dependencies_task_id) FROM stdin;
11	12
21	20
32	36
37	33
40	33
36	33
36	52
15	53
23	20
22	20
51	53
51	36
52	60
66	60
73	74
68	80
\.


--
-- TOC entry 3413 (class 0 OID 119221)
-- Dependencies: 219
-- Data for Name: tasks_progress_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tasks_progress_items (task_id, is_completed, sentence, issue_id) FROM stdin;
32	f	posibilidad de agregar un par de entradas al glosario cuando se crea un nuevo proyecto cuando un usuario crea un nuevo workspace	22599747201513
32	f	nueva prop -embedable- en WorkspaceEntity 	78313368722477
32	f	glosario visual para las distintas listas en client	45874656975224
77	t	Ajustar los colores porque ahora el fondo de la card 100% dark es el mismo q el fondo de la app	7766558995837000
21	f	eat state	29050314696138
21	f	default state	38769800116589
67	t	Agregar input de texto para filtrar tareas 	3353638354986127
67	t	crear tarea si se me ocurren más ideas -como por ej agregar navegación- 	5710454628060621
21	f	gather state	10754349878719
78	f	google font, letra yester year (o algo así...es como una cursiva) para la frase.	8800771310945146
21	f	build state	13795242811185
21	f	create state	40025020867520
21	f	walk to state	44195215008870
51	t	crear componentes reutiilizables como el filtro de tasks por text input	25588991206754
66	t	clickear en un tag tiene q llevar a una lista de tareas con ese tag	3743441714960336
66	f	pensar un sistema q incluya el parseo de tags para otras funcionalidades (agrupamiento, flag de moderación, tipos más específicos de tarea, etc)	2048459934572872
66	t	crear un pool de tags para el componente Project Info / reutilizable para usar como un buscador de tags cuando se agregan -algo parecido a lo de craze-	1330117871194416
66	t	hacer que los tags tengan colores dinámicos cuando se agregan al pool	8658392573167998
74	t	Agregar nuevo rol, ROLE_COLABORATOR	617492514218942
74	t	crear un checker en jwutils para colaborators	8982081011546223
74	t	solo los colaborators pueden autoasignarse tareas	5920531400297463
74	t	revisar los cambios en los endpoints. El endpoint de autoassign solo lo puede realizar alguien con ROLE_COLABORATOR, así como el botón en la UI solo lo ven los usuarios colaboradores	337034245349014
74	t	crear una nueva entrada en ProjectSettings para añadir/remover usuarios colaboradores.	8629661105086445
65	t	hacer que funcione el overflow del menú lateral de workspaces. 	5157408695997855
65	t	arreglar el comportamiento del scroll del mouse con la web principal y el menu	1607423203370654
18	t	falta aplicar chequeos en workspace y user controller	82694402633693
18	t	check valid moders/owners/user updating project/tasks	43656670007919
27	t	Modificar TaskMiniCard component para q tenga dos modos de visualización	46070358514832
37	f	registrar en local el workspace y las tareas donde se trabajo y hubo modificaciones en un array q se limpie cada vez q se salva de manera remota solo las entidades donde se registraron modificaciones. 	11536641605094
37	f	refactor a manejo de state de manera local y depender menos del remoto	11689134970969
37	t	ui para grabar/savear state local on demand	23855476165504
37	f	definir el flow de cuando se guarda remoto. Por ej: en apertura de aplicación, cuando guardan local	22849063645947
37	f	crear archivo de types  para definir variables q se usan solo en el client o flujo de trabajo local. 	44425576631046
47	t	mejorar la implementación de las transiciones de la barra lateral (el container de proyectos)	23252801494207
47	t	Un bug separado pero es apenas cambiar el color de texto de los issues recién agregados en CreateNewTaskModal.	29021566883915
22	f	UV Map shader	37922249868432
22	f	visualización de items equipados	81239431640149
22	f	animator	54671721320537
34	t	new issue modal reopened	12858644811970
34	t	new task modal reopen	29562013893529
69	t	Agregar de a una tag por vez, sin limar un loop de api calls	3483115795520320
69	t	agregar la tag al pool de tags del server (por ahora solo local)	8174739401625297
69	t	agregar opción para remover tags	2627068342917085
17	f	config google oauth2	36384707317201
17	f	log with oauth / keep authorized with JWT	46888406808634
56	f	Taak/Tak(Neerlandés)	19109156610660
56	f	Tasca (Catalán)	50094693419906
56	f	Tàche/Tash	1915197324683
61	t	añadir elemento con nombre del proyecto arriba de todo en projectlateralmenu	6376211921836213
80	t	reutilizar la lógica del project settings	2998888998912705
80	t	opciones globales de la app, en principio opciones de tema pero a futuro puede ser tamaño de letra, fuente, etc	4685820626504977
80	t	edición de perfil de usuario para completar campos q todavía no son editables	4761457193615622
52	f	usando tags y un 'parser' de tags se pueden conseguir distintas formas de usar los elementos de la task para visualizarla de otra forma	16520467013727
52	f	un calendario también puede ser útil	21166147940426
52	f	una de las help classes puede ser "Board" y es básicamente una tabla o una forma de disponer tareas	27993477599247
52	f	se pueden agregar también encuestas para q voten los usuarios	59039002513395
52	f	CustomConfiguration Entity y sus props (glosarios -otra clase de java- 	6213500145733
52	f	controller, servicios, repos para manipular las configuraciones 	1042326705589
52	f	Crear interfaces del lado del client y una ui para crear/manipular configuraciones	178740522007
55	t	un tracker para moderadores y un chequeo con la id del usuario para pedir un update en caso de coincidir 	2346280731740
55	t	nuevo tipo de respuesta para la creación de un workspace: el DTO del ws y una actualización del JWT	2954910068705
55	t	hacer un update de la api cada x cantidad de tiempo y si hay discrepancias en las tareas asignadas del jwt local y de la lista nueva entregada por la api, pedir update del jwt	33145091884
62	t	vista de proyectos para home	732446700894875
62	f	filtro por proyecto para las tareas activas del home	7233399622559637
62	f	crear la vista "Created tasks". En el homelayout tiene q  haber algún tipo de navegación por pestañas	7323333607033649
60	f	crear un sistema de tags q implique parseo de tags, usando propiedades de tasks, user y workspace existentes	4543751367145674
60	f	utilizar los tags junto a filtros customizados para crear vistas customizadas. Los filtros son una propiedad q se sume a la clase de customConfigurations. 	8869796371736748
60	f	dos TASK_TYPES nuevos: MANAGEMENT y REQUEST, q cumplen roles específicos	527612273388889
60	f	Crear una serie de templates de views  (Se me ocurren "Murder board" con herencias, un tablero con grupos de usuarios, etc) así como la posibilidad de crear nuevos templates. Template puede ser una propiedad de la c lase customconfig	1770921615956178
73	t	Cuando una tarea no tiene usuario asignado, aparece un boton para autoasignarse la tarea	1405842412981093
73	t	Crear un endpoint independiente q chequee si se asigna a sí mismo y si la tarea no tiene a nadie asignado	2291219997161052
33	f	sistema de save local y de cargado de workspaces locales. 	15807440972807
33	f	la idea también es agregar cierto grado de diferencia ux/ui según el tipo de tarea. Documentación más parecido a un procesador de textos. Aunque en definitiva lo q cambia es el valor q se les da a los enum con glosarios hechos por el propio usuario 	39694937587867
33	f	sistema de autosaves al server cada una larga cantidad de tiempo. Las distintas copias persisten.	48931457611755
33	f	refactor de peticiones a la api por saves locales	71259120652483
46	t	arreglar la forma en la q se navega para cambiar el current project o establecerlo si es que no hay ninguno elegido	43032862585814
50	t	ciertas tareas de tasks solo habilitadas para owner/mods o usuarios designados. Updates habilitadas para todos los users.	12953284514057
50	t	botón para borrar workspace. 	15314710664652
50	t	añadir/remover usuarios como moderadores	3463574055792
50	t	limitar control según rol	1210436765535
50	t	vista para tareas bloqueadas/flageadas	1663304969896
50	f	flag system para moderacion / quizás marcar el Status 'blocked" como una prop separada o sacarlo del dropdown	3967129208647
50	t	invitar/remover usuarios del workspace	429277029399
15	t	Icono para intercambiar tamaño de tarjeta	90026786099649
15	f	añadir las columnas a la lista en la barra lateral de proyecto	31735469344367
15	t	Buscador/filtro de tarjeta por tag/nombre	62641309549404
15	f	drag and drop between columns	58897211040946
15	t	pool / top priority / in progress / testing / complete columns	64436616119920
54	t	Video Nº9 - Functions	28777561597801
54	t	Video Nº8 - Variables	18002436278487
54	f	Video Nº10 - Demo variables	5007020525567919
26	f	Sistema de estados con modificadores obtenidos por el Episodio, interacciones sociales, necesidades, eventos presenciados, etc. Cada estado puede desencadenar/desbloquear nuevas acciones q generan nueva narrativa.	10267825801176
26	f	Sistema de 'Episodios', que definen las reglas y condiciones generales del escenario actual. El escenario actual puede ser etapas de transición o períodos concretos definidos	12823858910284
26	f	otras facciones / naves q son simulaciones de muy bajo nivel. Qué define a la facción, q nave tienen y alguna historia básica. Fin. El resto se genera si es requerido..	32421398434813
42	f	dockerizar la api 	34362209096087
42	f	subir la db a internet	44193378253084
42	t	en definitiva crear tasks y darles las funcionalides básicas q ya están en la api y faltan en el client para darle forma a una release para testear	36441584149639
42	f	crear un sistema para poder importar los workspaces actuales para no perderlos en la migración a la db online	39347108207570
42	t	agregar funcionalidad básica para crear borrar editar workspaces. Ver tareas asignadas en la página principal de usuario. 	39479719297536
26	f	Sistema para generar una historia q narre los hechos ocurridos durante la criogenización	32806064757637
26	f	condiciones para congelar o descongelar al protagonista.	10979239983064
26	f	npcs, generacion de biografía, generador de traits base, generador de traits secundarios por interacciones con el jugador	23131237910939
26	f	generación procedural de escenarios vinculado a lo narrativo.	36727845925976
68	t	por lo pronto, una simple página para mostrar info del usuario, del perfil y cosas como tareas activas, si es moderador, etc. 	4677089466547120
\.


--
-- TOC entry 3414 (class 0 OID 119224)
-- Dependencies: 220
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, roles) FROM stdin;
4	ROLE_USER
5	ROLE_USER
6	ROLE_USER
7	ROLE_USER
8	ROLE_USER
2	ROLE_USER
\.


--
-- TOC entry 3416 (class 0 OID 119229)
-- Dependencies: 222
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, about, avatar, created_at, email, github_profile, password, soft_delete, updated_at, username) FROM stdin;
8	Best guitar player in the band	https://lastfm.freetls.fastly.net/i/u/ar0/efe8bc6e11a3e52b46abf886a89f4c4a.jpg	2024-08-27 15:05:18.166196	georgeharrinson@thebeatles.com	\N	$2a$10$FPPzEdvj/RvwwrkLeu4cxe2Yd.uB597ZBd9xoUepz8Jf6c0inL8Cm	f	2024-08-27 15:06:03.964619	georgeharrinson
3	test	https://i.pinimg.com/originals/25/78/61/25786134576ce0344893b33a051160b1.jpg	2024-07-18 19:04:31.304161	example@example.com	\N	$2a$10$qgbiZAUzYWHeXN.G7wFQ9uePOzliCYckcq6JT4PTzJmScJf5zaPfO	f	2024-08-21 12:57:09.813803	example
5	delete test acount	\N	2024-08-21 17:53:26.374476	delete_me@delete.com	\N	$2a$10$2mTWRTEB6UiDu..4LSbjm.TzsYYUF9Z49.JK6P.ckALDC2Ule8hHO	f	2024-08-21 17:53:41.925951	delete_me
6	best drummer ever	https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbUVRlFCHI-zan4iAEzJk1vPcqlyE_q8brng&s	2024-08-21 19:43:25.368118	ringostarr@thebeatles.com	\N	$2a$10$fO7P6hbzAjkI.2QqDVyc7Oh/JWblxWq2mCfMEKOWEGX2KhkrRaBoS	f	2024-08-21 19:43:49.159004	ringostarr
7	second best beatle	https://indiehoy.com/wp-content/uploads/2023/09/paul-mccartney-foto.jpg	2024-08-21 19:49:13.957696	paulmccartney@thebeatles.com	\N	$2a$10$ijjbCby/xsLOyLOnsoaBQeLiXNlpP5HthfkB1ZtNnpLAf0KzeAo/u	f	2024-08-21 19:49:46.966587	paulmccartney
2	test account	https://mrwallpaper.com/images/thumbnail/cool-smiley-profile-picture-6lqzc2aegkuxbini.jpg	2024-07-18 19:02:12.167131	ltizzi83@gmail.com	\N	$2a$10$1X3ODX///P5odWMJZSa3.evzTpTJVbho7x/FbKCbY0vfUnAdbgdie	f	2024-08-28 01:19:17.155403	ltizzi
4	Best beatle ever	https://beatlesporsiempre.b-cdn.net/wp-content/uploads/2022/12/John-Lennon.jpg	2024-08-19 20:16:12.469045	johnlennon@thebeatles.com	\N	$2a$10$KfqOoNrQ8X4WRxuzhjYDcOZDB1xhyhyU186xWIX7hMVh6yxBaHnem	f	2024-08-19 20:16:45.467586	johnlennon
1	Dev-cards creator.. Father of Tita.\n\nLorem ipsum odor amet, consectetuer adipiscing elit. Nam fermentum consectetur eros platea a congue ultricies id. Venenatis per morbi nisl placerat tortor. Himenaeos ipsum erat tortor neque nunc mattis eros rutrum convallis. Platea interdum a sociosqu, faucibus imperdiet commodo. Nec elementum auctor massa ac ex purus turpis. Dictum nascetur hendrerit suspendisse id, maximus viverra blandit litora. Magna tempus feugiat lacinia nisi facilisis faucibus viverra. Ultricies mattis facilisi auctor faucibus elit.\n\nOrci integer justo per vehicula phasellus penatibus mus. Nunc fermentum nullam laoreet facilisis nisl lacinia enim. Lobortis vivamus class ornare tellus taciti dapibus nec suspendisse. Eleifend amet finibus; cras risus erat ornare sagittis lacus. Quam parturient tempus tincidunt luctus pretium venenatis tincidunt. Nam justo curae consequat neque pellentesque vulputate cras volutpat nisi. Eget etiam libero erat fames scelerisque commodo. Aliquam mauris class turpis ultrices nullam maecenas. Ridiculus maecenas senectus gravida proin elementum fusce?	https://avatars.githubusercontent.com/u/94137791?v=4	2024-07-17 22:29:16.912533	admin@admin.com	https://github.com/Ltizzi	$2a$10$CIMHcqD2nSCNtZMu8Bi0F.r0AB/hQpDIN8kkIrleDuwhd2S8aUsnC	f	2024-08-29 01:23:52.409934	admin
\.


--
-- TOC entry 3417 (class 0 OID 119236)
-- Dependencies: 223
-- Data for Name: users_designated_tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_designated_tasks (user_entity_user_id, designated_tasks_task_id) FROM stdin;
2	10
\.


--
-- TOC entry 3418 (class 0 OID 119239)
-- Dependencies: 224
-- Data for Name: users_workspaces; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_workspaces (user_id, workspace_id) FROM stdin;
3	1
4	1
5	1
6	1
7	1
2	1
2	3
2	14
1	3
1	15
1	21
1	1
1	16
\.


--
-- TOC entry 3424 (class 0 OID 142155)
-- Dependencies: 230
-- Data for Name: workspace_collaborators; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.workspace_collaborators (workspace_id, user_id) FROM stdin;
1	5
\.


--
-- TOC entry 3419 (class 0 OID 119242)
-- Dependencies: 225
-- Data for Name: workspace_moderators; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.workspace_moderators (workspace_id, user_id) FROM stdin;
3	1
1	2
\.


--
-- TOC entry 3420 (class 0 OID 119245)
-- Dependencies: 226
-- Data for Name: workspace_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.workspace_users (workspace_id, user_id) FROM stdin;
1	1
3	2
3	1
1	2
1	3
1	4
1	5
1	6
1	7
21	1
15	1
16	1
\.


--
-- TOC entry 3422 (class 0 OID 119249)
-- Dependencies: 228
-- Data for Name: workspaces; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.workspaces (workspace_id, created_at, description, project_avatar, project_name, soft_delete, updated_at, user_id) FROM stdin;
3	2024-08-02 01:11:46.972348	test	https://s3.envato.com/files/322951106/demo_ava_01.jpg	NPCs	f	2024-08-02 01:11:46.972348	2
6	2024-08-08 14:52:05.566404	this is a test project to test the new functionality to add projects	https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/37e7be51-44e0-4b80-8339-9f4f80026dfb/d2wh0s0-b74aa555-26e4-4602-9cb2-ad5478571ceb.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzM3ZTdiZTUxLTQ0ZTAtNGI4MC04MzM5LTlmNGY4MDAyNmRmYlwvZDJ3aDBzMC1iNzRhYTU1NS0yNmU0LTQ2MDItOWNiMi1hZDU0Nzg1NzFjZWIucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.S5VQABzZ-z_IWPqYMn68CXZEnsE4f4KEIAhQe7Pq5Fs	test	t	2024-08-08 14:52:05.566404	1
7	2024-08-08 15:00:36.608659	another test project	https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn0LOLuXYDHE25FOE27Hea6s8Z4iEL6do5eg&s	testing 2	t	2024-08-08 15:00:36.608659	1
8	2024-08-08 15:02:48.240518	asdasdasdas	https://alfaomegaeditor.com.ar/wp-content/uploads/2023/02/unnamed.png	another another test	t	2024-08-08 15:02:48.240518	1
9	2024-08-08 15:06:43.687201	asdasfasfasf	https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT45j6d_wP4Dxv_y1ZGhB3Z-GGpUM9PyztaWw&s	asd	t	2024-08-08 15:06:43.687201	1
10	2024-08-08 15:10:50.187161	asdasdasd	https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHnf-yIYhPw_hByn1vwEsGN2bD-pOJJocPjA&s	afdgagasdgadg	t	2024-08-08 15:10:50.187161	1
11	2024-08-08 15:12:57.474048	aaaaaaaaaaasd	https://e7.pngegg.com/pngimages/217/193/png-clipart-homework-test-education-student-computer-icons-student-people-essay.png	aaaaaaaaaaa	t	2024-08-08 15:12:57.474048	1
12	2024-08-14 11:08:48.912424	test	https://s3.envato.com/files/322951106/demo_ava_01.jpg	NPCs2	t	2024-08-14 11:08:48.912424	2
13	2024-08-14 11:09:44.768897	test	https://s3.envato.com/files/322951106/demo_ava_01.jpg	NPCs2	t	2024-08-14 11:09:44.768897	2
14	2024-08-14 11:21:31.150605	test	https://s3.envato.com/files/322951106/demo_ava_01.jpg	NPCs3	t	2024-08-14 11:21:31.150605	2
15	2024-08-17 01:31:20.30997	Proyecto para anotar data y recordatorios sobre cosas	https://www.randomdraws.com/content/img/global/logo/white-bg/randomdraws-square.png	RNG	f	2024-08-17 01:31:20.30997	1
17	2024-08-19 11:01:47.292882	this is just an example to test	https://cdn-icons-png.flaticon.com/512/5499/5499405.png	ExampleProject	t	2024-08-19 11:01:47.292882	1
18	2024-08-19 11:12:14.940542	example to test new functionality	https://cdn-icons-png.flaticon.com/512/5499/5499405.png	exampleNew	t	2024-08-19 11:12:14.940542	1
19	2024-08-19 11:19:19.448709	example example example	https://cdn-icons-png.flaticon.com/512/5499/5499405.png	example	t	2024-08-19 11:19:19.448709	1
20	2024-08-19 11:24:39.988659	super example	https://cdn-icons-png.flaticon.com/512/5499/5499405.png	super example	t	2024-08-19 11:24:39.988659	1
21	2024-08-26 10:24:22.241747	Portfolio para psicóloga	data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUTExMWFhUXGB4bGBcYGB8fIRkfIh0aIh8gGh0iHSggHR8lHx4dIjEiJSwrLi4uGh8zODMtNygtLisBCgoKDg0OGhAQGC0gHx8tLS43Ky0vKyswKysuKy0vLS0tLS0tLS0rNTAtKysrKysrLTA1Ky0tLS0tKy0tLS0tK//AABEIAOEA4QMBIgACEQEDEQH/xAAcAAABBAMBAAAAAAAAAAAAAAAEAgMFBwABBgj/xABHEAABAgUCAwQGBQkGBwEBAAABAhEAAwQSIQUxE0FRFCJhcQYjMoGRoQdCUrHBFRYkMzRTcqLRYnOCkrLwQ2PC0uHi8YNE/8QAGQEBAQEBAQEAAAAAAAAAAAAAAAEEAwIF/8QAHREBAQADAAMBAQAAAAAAAAAAAAECAxESITFBBP/aAAwDAQACEQMRAD8AslOodi9RbxPrXPbv4MekaOiE/pN//Nst/wATO/udoe06hRVo4s573Ke6WDDbEAnVpgmdnxw7+Htm17d+rc4Ak6j231FvDfvXPdt4MI0lXYMH1nE/wtb8Xd/lD2oUKKRHGlPeCB3i4Y74hrSx224z82Nbbjd3f4CA0dKNZ6++y/FrOzY3cdI2db4v6PY13q7rnbk7N8nhiv1JdKsyZTWJ2cOchznzMHztIly5Znpe9Kbw5w7Pt5wAwouw+uKuI/cta3fLvn7PzjDI7f6wHh2d1vafm746w3ptWqsXwpzFIF3dwXDD8TCtTnGjUEScBQuN2c7QCxrXZvUWX8PF1zPz2Ytv1jX5FNN6+++zNtrO+N3Lb9IJo9Jl1CBOmPevKmLDpt7ojqTVZlQtMmY1iyxYMeuD7oAlU/t/qwOHZ3n9p+TcoxNZ2H1JHEu77vbvhmz9n5xD+mWv0+khPDLzl/8ADJc29T9kPz55bYtUPpF6eVtWu5SggMwCAzDO/wAecBd87TkK/SFTkIu9ZapsPlnKh8WhSdfRW/o6bUlf1gsKZu9th9m98eYp6io3KLnqfwhqXNX9UkAHrziD1MlPYMn1nEx9lrfi+8YdNNZ6+7hvi1rtvFxHn6q9Oa8yU06p5UEE2rPtgY7t75GMPnx6RdH6R1UsuipqEnfuzlj5AtFHpb8tY7LZ/wAq+7/Dcze9njQ0/sXririfVta3fm7npFE6P6fVctYWtXGZV3fAfd3dIBOepi1fRH02l6osSJqgkkPw/ZJI+yfrc8DMB0Kk9vyPV8PH2nu+DbfONjVuyfo9l9n1nZ372zHq28a1RXYikSMXuVXZ2Zm+Jh+g0yXUoE6a96ndiwwSBjyAgGPyGZX6RfdZ37bWfmzv82jaqzt3qQOHb37nudsM2PtfKBpOrzJqxIURYpVhYZbbeDNSpU0aRNkuFE2m7OCCfvSIBCajsHqyOJf3n9luTNnpCTohqfX32cTNtrty3cPtDmmSBWJK52VJNotxhgfxgSr1aZIWqTLaxBZLhzs+T74Ak612r1Fll+LrnZs7MH2jQk9g75PEv7reyzZ8YIrdKl06DOlvejIcuM428jA2mTTWEonZCA4txnaA2aLt3rgrht3LWu2y7uPtfKNjW7P0ax7fV3Xb/Vdm97PDepVSqNfCksEkXG7Jckj8BBkrSZa5YnqfiKTxDnFzPt0eAE/M8/vh/k/9oyAfzoqOqP8AL/5jIAjWhM4v6Pfw2H6p7Xy/s4eJUcHgf8Pi8P8As332/wCa673vDGnVyKRHCnOFuVYDhjtmATpMwzO0MOHfxHfNr3bdW5QGaMJnFHaL+Gxfiva/L2sPD+v7o7N43cH3Ndb72fxh/Ua5FUjgyXKyQchgw3zDWlq7FcJ+L2ttzs7v8RAFaRwuEnj2cTL8Vrtyz3Z2+UQ9MJ3FTfxOFfm6621+b4tb3Q9X6cuqWZ0oAoUzOWOAxx5iJCdq8uZLMhJN6k2Bxh2bfzgE65Zwx2a2+4PwmuZi/s5Z2+UI0G21XaWuu7vG3ZhtdlngfTaVVIvizmCSCkW5ySDt7jCtTkmsUFyMhItN2M7wAmpidxV8HicN+7w7rWYey2N32hH0g+kdPRUilyxLM5TJRYzgnm4yMA45seQLS9LrEqmliVNJCpYNzBwOe/kY88emGrmqqBLSfVodvP6yj5MEjwS+6jARU6qm1Exc2asqUS6lqL//AHGAI3wMZDDdjufFX4CJOnpUhIDeQPM9T/voIHqzgnlsPE8z5D+keVRXCK1eEPTEDiBA2Tk+4f1++JCRKEtNx3Z/6fNz7hAmmIuU5+sfkP8AywigSpQ0zPg/4xkymYsRg/7xDk9YMwvs/wDv74lKeUFoKT7SMe6AjJVMbgkljuhX4GC5KC9wdExBcsWII2UkjYjw84VT+sTacKGx/wB/73EKWssF/XRhQ6xBcX0YelSKi+RWLSZ6QCkzCCFp5lL4CtnA8DttO6sJvFVwOJw8W8O63YO1uN325vFB8QpKZksspPfQejbj3beRi/PQz0skKo5Si4JBuADsbi490WImazgcJVnD4tvdttuubk2XeIzQwriHtN1lpbjPbc4ZrsOz/OG5GkTZaxPUBYlV5Y5bfaDdTqk1iRKk5UDcbsYAI+8iKB9dCr09me23vcF2dzvbh2aJLTeDwk8bh8Ru9fbc/wDafL+cCaXPFGkonYUo3C3OGA+8QJWaTMnrVOlgWLLhyx2bb3QDelidxUcbicPN3Eut2LXXY3bfwg/XrbU9ma5+9wd2bnbyeHq3VZdQgyZbla9nDDGd/IQLpko0ZKp+AsMLc53gCNDs4Z7TbfcW4rXMwZrss7/OIucJ3GNvE4V+Gustfk2LW9zQTqVKqrXxZLFIFpuxkEnb3iDZWrS0SxIUTxEp4Zxi5m36PAHPSf8AI/kjI5j82Kjon/NGQEinT+2+vKuH9W0C7bxxCTrZH6NYG/VX3f4XZvezwzq1YummcKSqxDAswOTvkuYkhpcoyePb6yziXOfaa52dt8ttACq07sXrwriN3bWt38cxiU9vyfV8Pp3nu+DbfOB9KrV1MwSpyrkEEswGRtkMYf1o9kKRT9y97vrOzN7TtuYDStVNH6gIvtzc7O+dmPWFnRBKHaL3t9ZazPzZ3+cP6Xp8uplibOTctTuXIdiQMAgbCIun1SbMmiSpTy1KsKWHsuzOz7QBaa013qSnht37gbtsMzD7XyjFT+werA4l/ecm1uTc+kPaxTJpUCZIFiyq0l3wQSzFxuBCNGlCrSpc/vqSbQdmDA/VaA4/6TqkS6I1VzKnqCQjpgvnyS231opCgVz5qP8AKD+J+6LL+nOoKTJpwfVoUspT0wjnudzvFUJmZYdG90RXQSqi51OyeR8OsB1NSFKAGw5fcPM7mBJk/DdIVQylrU0tClq6AbeJ6RAVqc92QDndXu/ACE002xBPNgkQ0ZRtUUpUr7cxsDOwPiR8vOJvRfRmZNsu7qpmUg/UR9aarpjCRzLchHm5yTte5hbeRATpZSUqI9oOPJyPw+YgujqWUFDmLT58n+6O1qvRXjrWECxDISh/qi0hH8qEKP8AeGOKrdLmybr0EWqtWD94+XuIOzt5w2TJ6z1ZYmpk1phbbcff8i/wg8zwe91Gf9+EQ6pjsXyIWic22x5dD0jq5CqedaSnoXH++hH4RZ30Q0Yn8eRxLbSFpDPgs/PxSPMGKhnLLv0jufos1RcqrJQpiqQoHY7KSdj5wF1flwzf0ewJC+5dc7cnZvxhS6PsPrgeJd3GIt3y75+z84LqtKlS5SpyEtMSm4FyWLO7EtEdo9QqqWZc83pCSoDAyCA7hjsTFQ8in7f6wnh2d1h3n5u+OsJOtmm9QEBXDxdcz89mLbwnWZxpFBEg2JULiN3Lt9Z+Qg+g0uVPlpmzE3LWHUXIc+QLQAitG7L68LvszazO+N3LbxpM7t/cI4dnecd53x4QJp2pzZ8xMqaq5C/aDAOwJ3AB3Ag7WZYpAlUjuFRYndxv9Z4BCq3sPqQniP37ibd8MzH7PzjY0QLHab2u9Za231md/c7Q7o9MmqQZk8XrCrQXIwACzBhuTEdN1SaiaZCVNLC7Alh7Lszs+3OAJ/O8/uR/n/8AWMiV/Nym/d/zK/rG4APRZ0uXLtnlKVuS0xnblvloilSJvGvZfC4lz5tsud+lrfKCqygVWK40ogJa1lli48gRBX5YlhHZmVfbwnYNc1u7uz+EArWp0uZKKZBSpbjEtnbntloH0AiXf2nuu1vE57uz+75Q3R6eqkVxppSUgMyCSXPmAIXXJ7cQZOOG7349pmZn6GAE1iVMXNUqQFqlkBih7dg7NjeJmqqZJkqShSDMKGADXXNsObvA1JqaKRIkTAoqS5JSARnIZyDsekCS9GmS1CoUU2JN5AJdt8Bmf3wGaGhUuYVVAKUWkAzNncNvh2f5wvXQZi0mndSQGUZezvzbm0P1tYmtTwpQIUDe68Bhjk+ciE0M8UQKJrkrNwsyG2y7QFT/AE4JaXQv7SRNTMHMEmWU3c9n3iraGSVrAG5LCL6+k/0bXW0q50spcqC5SCWUojBSzM5D82wI4P0A9EVpqFGpllHDDhJbJOxBDgpwcjpHPZsmM9uuvXc7JE3Q+glHKl+uUVKa4qUWYDdhyTnffxiLqdH0YqKL1pVs4mH7iTj3RMen+nT5ypKJSiEqUQMgWqCFHDC5yEkbsXZsxXUrS0TJ0uRLBJWACVlu8xJYjDdMRlwlynblWvbccL4zCelh0HoDT3omSp8xUsfVUQrm+C3djtRIS5NodXtFt/PrHHegmm1FLM4MwmyZLUtIJe21ZSfdhx5x3NzRx2d7y1p0+Pj3GcIRLHTm/v2+6Atf0jtElUtKghamZdrsxfZw/Pnzg4VAut8HP4fjAnpHWLlSFKlB5iilEsdVKLCJh99Ln89q/mfReQCO2odgwVLAz53OA/QbNHP6t6C1ssKmWImJDuqUQdsE24O4LsIDrhVLmAqnqUtWQEKJYnkw5+EdN9HvpHPlzjSzApbuyOd2OZ2Hw35mNkucne9YLNeV5yxX00OPERYn0DhHbVzJhSEop1DvMzqXLbfwBgr6RPRziTZZppLrWTelLDbJU5YDJAJ54iX+jD0GnolTZiglClKCbVKyAnLm0EZKjz2Ajrr2TOOOzXcL7djSU85M1KlpWJYU5KnttfnyZolNdmImSwmnIUu4EiXuzHdss5Hyjc7WZc1BkJCr1CwEgM+2cu3ugaipFUSuLNYpIsARkuWPNsd0x0cj2grTLQoVBCVFTgTN2YbPyd4jtSkTVzVqlBZlk90oe1mGzYgytkGtImSmASLTfgvvhnxmCKbWZdOkSFhRUjBKQCOuHIPOAe1SokrlKTJUgzC1oQ1xyNmzs8R+ggy1KNQ6Ukd3ibO/J+cJptImUyhPmFJQjJCSSchsOANz1h+unCtARJwUG434wcYZ4AfXEKmTAqndSLQCZezuX2w7N8olKepkiSlClI4tjEFrrm2PN3gSirE0STKmglRN/cyGOObZwYGVo8xa+0ApsUriAEl2e7ZmdvGAjOxVX2J380ZHSfnZI+zM+A/7oyACnagaM8FACx7Tq3z5eUPfkVJT2m43NxbcM/tN1Z8Q5o1Iiol8ScL1uRccYG2zCIpWozRO4N54fEstYezczOz7YgC5OoqrDwFgIB7zp3x5wqoPYGCO/wATe7lb0bzgnWaRFPLMySmxbgXDODvu4gfQR2q8z/WWNa+Gd32boIBUnSk1Y46lFJVghLMGxz8oZTrSpp7OUgJUeHcHdtn84Y1etmSJqpUlRQhLMkAFnDncE7mJiq06UiSqalAEwIuCnOFM77tvABz6IUQ4yCVk9xlbZy+P4Y1TyO3OtZsKO6Annz5wxolQqpmGXPVegJKmLDIID4bkTC9dmGmWlMg8NKg5Ay5dubwEfqqrViQDiUClJPN2Jfx/pEMCoT0blKrkXFskZbHL2o7FFLKXSmomovXYpZLkElLtsQxwNopuk+kBRlo48p1pKVBcvDkdUnqHGDz2jHv0W5eUbf5/6JjPG/ixFJBI8CCPAguCPEHMRtT6O065nFMiUVu7utIJ6lKVAfAB4mqeyahMxBBCgCCNi+xHhDiaeMs8o+hbrznaHpaRlqmKIKykJDBglI2SgcgN4IXaNyB5mEz1lADS1LD5tIceLEh/IZgebX06g0xQSOkxJR/qA+Ii/XPsnqFWoMxgoFRGwzs/9YVXUlyQOaFJWl/tJUCH8HEM0tVSS8omS89FOfINEhKmCYgKS7EOHDH4HMJP1Ln31XD1HobLM8Tpa5sg3XWpSlQSf7Crg46XDzeJqbo0kzzPUl12JQCdwASXJS3eJJduTDlE0qTDFUAlBUogAc4tzy4uOvXL1EcS6esAlISAh2fJyzlwHcb9BExpmrrpyoABTgAvjIfp5xXEj06ClolypJPEngFajyUoBwltwk8+m0XJoelyZkhCloClFwTkbEgbHoI0aNNmXlkx/wBO/HKeOJuZoiZQNQFqJQLwksxO7eUNSKs1x4SwEBIvdO7jDZ/igOl1GbMmplLWShSrSlhkOzbPEnrkhNNLC5AsUVBJIzhiWy/MCNjEYn1BoTw0C8K7xKuXLl5Q7L0VNSOOpSkleSAzDlj4RmhSk1KVLnjiKSpgThgwLYbmYjtS1GbJmrlS1lKElkpAGMDqHgCpWsKqiJCkhIXgkbhs8/KHKiT2FloN5X3SFcmzygzU9PlSZSpspIStLWqBOMgc8bGI/QphqVKTPPECQ4BwxfwaAdkUYrRxlkoI7jJ2xl8/xQ0daUg9mCQUpPDuy7ey/nCNbqFU0wS5CrEFIUQGOSSHy/ICJOn06UqSJykAzCi8qc+0zvu28Ax+aMv94v4CNRBfl6p/en4J/pGQEhrdKufN4klJWhgLh1DvvEsK+VwOFeOJw7Led1rN5viBKfUBRjgrBUr2nSzZ82hg6Msq7Tcm1+Lbl2e5tmdoBvRaVciaJk5JQhiLj1O20EekA7SUcD1lr3Nydm38jC6jURWDgoSUqPedTNjyeE0quwuJnf4m1nK3q7dYArR6yXIlJlzVBCw7pO4ckj5RD0unzUTkzVIIQF3FWGAd336QVP0tVWTPQQlKsAK3xjk45QQvWkzUmnCVBShYCWZ9n3doBWuz01EsIkkLUFBTDoAQ+fMfGEaBMFOlSZ54ZUpwDzDAcoZp6M0R40whQIsZO7nPNukbqZBriJkvuBHdIX8cM/WAC1TT5k6auZLQVoUcKDMQwHWKF1Sj4E+bTrGAo2Hql+6x8mzHoyTrSaZIkKSpSkYJSzHnhy/OKn+mKj4CpEpaUKKwSlYdxkBhsTvttmJRGfR96ZdlJp559Q/dX+7J6/2Cf8pPQ4uFCgoAguDkER5oluicy+W/j0/rFkfRRqbcdClzAnBlSwxAyXIB5bOA28cNmny9xo17vH1VntAs2gBLgqST02+BxBU8qlyePMMsSrbioqCS3iDh/AEwKnVEWoUUzEhabkvLVkeTOPfGe68sfrVjtmXw5SUYRlyo9S33CCYjZ+uSUB1FQGzlJAzs5LAP4wYhS106qhFoQlKlOS5ISHLBIIPxhjhll8hlsxn2nFqADnAitPTX0oTPJp5JdH/EWNiPspPicE9MdYhNT9I6irQ61skhwhOE+R6++OaRPKnljFx7x6DLgeJ28MmO+Gnl7kz7N/fWLtPogpKZVcZ05YHBRdJSRhRe0r80uMf235RaOr0UyfNVMlJK0KZlBmLAA/MGK7+i/wBH1VM6ZNlsmXJQJYJ2KlMWGOQT/MItKRqyaRPAWlSlI3KWbPew5B5xpjKMrNQlLkqlIWCtSbQnmS20RehSFU8wrnixJSUgnq4LY8AfhG0aKuUrtClJKUG8gO7bsMM8PVNYK0cKWCkpN7q2YY5PnvQDOvSjULSqQOIlKWJHIuS2fAxJaZqEqVKTLmLCVpDKSeUBUtQKEGXMBWVG4FGzbc26Q1N0ZdQTPSpKUryAp3HLLDwgBtKoJkmaiZNQUoS9yi2MEdepESOvzBUpSmQeIUlyByDeMZO1lNSkyEJUlS8AqZg2csX5Q1TSjQkrmd8L7oCOTZy7QBGhT008sonmxRUVAHowD48QfhEXOoJqpxmpQTLK7wrDWu779ILqKM1p40shIAsZW7jPJ8d6Hka0lCRTFKipI4dwZn9l93aAlPy5T/vU/P8ApGRz/wCaU37aPn/SMgDKTT01ieNNJCvZZDAMPMEwMdYmBfZmTZdwnYva9ru7O3hCNanLlTbJBKUMCyNny8SwpJXA4lqeJw7rud1rv5vmAFq9PTRp40oqKhhlsRnyAMIok9ucze7w9rMe1u7v0EDaLOXNmhE8qUhiWXs/KH/SD1JR2fuXPdZzZmdvMwCKrVF0ijIlhJSnIKnJzkuxA3PSCpmjS5aDUJKitIvAJDPvnDt74d0emlzZSVzkpUsu5VuWJAf3RDUtVNVOShSlGWVsUna19j4NAGUdYqtVwZoCUgXujBcMOb4yY3WzzQkIlMoLFxvyX2wzdII12SiTLCpACFlQBKN2YuMcnAhGgS0zkqNQAtQUwK9wGGz+MAqn0ZFSkT1lQUvJCSG6YcE8usVj9MOrdop5UtctBm8R5SkuClh38vsQGbqx5R3ep1U2XNWiUpSUA90J2GBt74hfpn0WnFCmoRaibJmJKGHt3EJKC3UF3/swFGT5vEVLJ9ohle5947f0Lq6aUJlRMJNrSpUtBZSibSVKJBCUeyH3JdhiOLtC5oxaSe8noYl508Jl2jFqse4jfxjyroNa9JZ1QZfFLypSk2y04CWOQocyRi49eQMWt6Ra/TyKaVUKUSCEWhAKipK7Q7DYbFz0I5xREyYVTFFOWLEP7Xh4+XOOgX6QIXpCqQ4mJnJc81SwQUueqVMG/sjxZZ2cqy8vYsDVq+QvTJs8G4TUnBBSQWUEpYsQXw/UlsNHGeiHphNoUcMkTJczPCIdn3KA4IB5g4Pm5MFU66ubTSKdQNtMVp8ZhK1cNONwlLD59CY1M62YCS5J77bAuMDyEJOeoW9vaCqK0cWYqUm2UVqCUv7IcsM5GPvaB6KnK1rzaLi5gZf1i+5yOh5e6CKKWVJUolkZJ8fPwiovH0AqplLp1MpKQDOCiyh9UKNhw3tAu53cR1tLpSKpInzCoKW7hJAGDbhwTsOsRX0eXT6SWasXKEuW14ZnTyHLlDur1EyXNUiSpSZYZgnYYBLe94qHpetTJqhIUEhCzYSAXbbGWf3QRW0gogJsolSlGwheQxc8gMukQZWUcpMlS0JSJgS4I3BbceMRWhTFTphTPJWgJJAXs7jOebEwBFFTiuBmTXSUm0WYDb5d85hio1mZTqMhASUowCoF+uWIHPpG9eWZK0ppyUJKXIRsS53bm0SWm0kqZKSualKlkOoq3J8YAep0dFMkz5ZUVIyAohs4ywB2PWGKKaa4lE3uhAuFmMnGXeA9LqpsyahE1SlSy9wVscHf3tEjr6BJSk04sJLKKOYbm0AzWVaqJXClMpJF7ryXLjk2MCCJejS1oFQSq9Q4hAIZ/a6Oz+Mb0KSidLKp4C13EAr3ZhjPJyYi51VNE4y0qUJYXaEja12byaAV+dk77Mv4H/ujI6P8mU37uX8BG4CN0isRTS+FONi3JZicHbKQREarTJpm8ez1d/Eucezdc7O+2WZ4Kmaea08dKrB7NpD7ePvhf5aAHZrC/wCqufD+y7fNoB7Vq1FTL4Uk3rJBZiMDfKgBDGikUlwqO5e1v1nZ39l23G8Jl6caM8dSgsDu2gNv4vCpqe35T6vh9cvd8OnzgBdU0+ZUzFTZKbkKZlOBsGOCQdxErUapKXKVJSp5ik2BLHKmZnIbeBUaqKQcApKynNwLO+dvfCBohlHtBWCE+stbfmzvAN6PTKpZhmTxYgpKXwckggMlzyMK1mUatSVyBelIYn2WO/1m5Q5Mre3epSLCO+5zthm/xfKMlT+w+rUOJf3nGG5ePSAK07U5UiWmVNVatAZQYlsvuARsY5PW/RFc6SuXPHDlkPxAQSgjKVMC5ZTY6PE+rRTUkzwsJEzNpDty3fwhxesipHACCkrxcS7Nnb3QHnKsplSawSpikqUhQTeh2U4DEOAdiMERlXIdMwdCk/HB/CJP6TKaZJ1GZxEtZaUG1r0ADPiXf5dIZnS/WKT9pGPHLiIqdl+h6VyRMp1qXNMsKCXGS31sAJfbJ+MQtXp05F5nSJiQkATSUlh0KlDAII9p856R0/o36WppqREkSFKWkqJUSlKS6iRnJLBhtyiV1L0mlTdKnMtAmzJiULQFuQO6TsXtPeYxzmWVy5z06XHCY977VdPqLcZCyCT/AGX5DxZgT0xC5unz0UqZ9lktShYpRAMx/wB2n2ldXZmBzCJtKQbiD/EGWPwPxjoqb0wqikoVMlTHBTcRYsA8kqAAHvBj1e/jzjz9cpU0oMszBuFMofGJ36M6Knn1suXVOZCQpZTyUpLMFDcp6gb+TxF6nTrTJURgXgsCDjvdOjiJL6NdGmVddKlpNoS61KPJgpvfc2POPTyv3Wk9rKDTi8IBCvqs7N7TdDtBWmajLp5aZM5Vq0u6WJZySMgEbEQPLV2DCvWcTOMNb8esJXpJqyZ4UEBf1SHZu7u/hFQJTaXNlzUzloaWlVxU4LB3dgXiR1mpTVIEuQb1BVxDEYAId1MNyISvWxNHZwggr7lz7cnaES6Q0J4qjxAruMA2+X/l+cA5os5NIlSJ/cUpVwDO4YD6rjcRH6hpc2dMVNlouQsulTgOGA2JB5QZMp+3HiJPDs7rHL835dYUjWxTDgFBUUYuBZ+e3vgCdR1OVPlqlSlXLX7KWIdiDuQBsDAOiyzSKUqeLAoMk+053+q8KRoxpSJ5WFBGbQGd8bv4wqbO7f3Ejh2d5zl3x4QDOsUyqpYmSBegJCScDIJLMpjzESEjVJSJQkKU0wIsKWPtMzOzb89oGl1nYfUqF5PfcY3w38vzhv8AIpWe03gBR4lrZ+0zvARn5uVP7r+ZP/dGRMfnen90r/MP6RuAGr65dIvgymsa7vBy53zBv5IlmX2jvcS3i74ua7bo/KN6IJZlvUWmY5/Wtc3L2stEQpc7jMDM4XE271ll3+W233NAE0FcurXwZrWEP3QxcbZh3Uj2IpEn/iPddnZmbbqYJ1oSxKJp7RMcfqmubn7OWgf0fY39pztbxve9t3udvCAdodNRVIE6a96ne0sMFhjyEAydXmTFiQq2xRsLDLbbvu0I1hUwTVCQViXhuG9uwdrcbxM1SZPBUUCXxbMFLXXNybNz++AF1CkTRpE2S9xNveyGOfDoI1psgVoK526TaLcY38YG0IrMwiouKLS3Fe13De1h2f5wvXyQtPZnCW73Cdnfnbh2gG6vVplOtUmXbYjAcOdnyX8YPq9Jl06DOl3XoyHLjpke+HtLTJMpJnCWZjd7iNdufafO3WITTFTTNQJpmGW/evutZju+N23gOc1/0TmazPQZtTwkyklgmUDhRDsbhksN3Ztt44z0u0OXTVK6fiEcNrDcCSggFL7ZbD+EXPr4SEp7MwVd3uFuzHe3LPFKa4lM6ZOnrdSlP3iSXADJf3AfCJViAqESQ5Mxa28f/pgeXUJSQUSQrLd5yDg+L/BokVU6RI9kfVjeozUJSgqUAQp25nHIRBESquYkKUz2qYthoe/KEtQBmJ35sx+IjWnKBlzj1Lt03/rBhQlVOCwLHp4wBGmaYieoJkoXMUSBaPHZ+j+MXFpvojK06TLqEJCakMFqB7pcF0tzSHYeUcV9GtShE9rUvOkKSC310glBB5HCgPExYuiKWZrVBWZdp/Wva+G9rDxYUTpqe23Gd9Rgm3G+779BDFbqcylWZMprEbXBzkOXL9SYd9IMFHZnAzdwfc11vvZ/GDdHTKMpJnhBmZu4jXblnuzs2/JoqG5+jy5SDPTdekXhzh99ukCadVKrFGVOa0C8W4Lhh48lGBKRc7ipCzM4d3euuttfm+GaJTXggSx2e0LuD8L2rWLvblnb5QA+ozzRqEuT7Khcbs528OkFUukS6hAnTLr1hyxYdMD3Q3oASUK7SxVd3eLuzDa7LO8RupLnCasSjMEt+7ZdazD2Ww3lAP0erTKhaZMy2xeDaGOATgv1EE6lJFEAuTussbs43xtBeqpkiUoyRLEzFvDa7cOzZ2faI/QCSpXaXKW7vGdn8LubQD+nUiaxJmznuBtFuAwY+PUwGvV5iFmnFtiVcMYza7bvu0K10rEwdnuCLQ/Ce13Lvbh2b5RKU6ZPBBUJfFsyS11zc+dz+94DX5rSP7f+b/xGRzPFq+tR8VxkBKalRLql8WSAUMA5LZG+DB41WUJXZ3PEs4bMWua1n235wKdR7F6gJ4n1rnt35Mx6Rv8AIj/pN/8AzbLf8TO/udoBjTaFdKvizgAgAhwXydsCHtWHbCkyO9Y9z43Zt/Ixg1HtvqCnhv3rnu28GEbUrsGB6zif4Wt+Lu/ygH9O1GXTSxJmkhaXcAEjJJGR4GI6RpM2XME9QAlpVeS+Wd9vKChpXbPX32XYtZ2bG7jp0jX5bM39Hsa71d1ztydm+TwDuqVSatAlScqBuIIbAcbnzEJ0mcKNKkT+6pRuDZwzcoSqi7D64K4j9y1rd8u+fs/OMTI7f6wnh2d1vafm/LrAC1ulTKhap0sAoWXSSW5AbHyiTrtVlz5apMskrXgAgjx390CnWjTeosv4eLrmfnsxbfrGzovZvX332ZttZ3xu5bfpAROoTzp1NUT5yRmUUoAPtLPshxs5YPHn9FdUKCUXKLguw5CLa+lX0iNRLlU4Ra67z3nfcJGww5J/wxXMwgGaobIAlj8YlWISWZ0xQQpSiN2ctjwgeTI7kwtsRyiU0v21K6IUYaokPJqPP+sAFIlEqbmUY+AMOLmzEywyjaeWP97GH3tMlfVKf6H5QT2cFExH2FFvI/7EQS3oFqRl1lGZheXxQkvyJJtI97fGL+1OtRVI4UkutwpiGwN8mPNWjOZah9aWQpPgUlx9wj0Rp8tMiVLrEK4iZiElKWtwsAjOfuiwojSVdjuE/u3ta2dnfbzEDahpsypWqdKAKFMxJbYAHB8QYJCe35Pq+Hj7T3fBtvnGjq3ZP0ey+z6zs797Zj1beKgqp1aVMlmSgkrUmwAgjO28A6XTKpFmZPDJKbQ2ckg7DwBh06Hwv0i+6zv22s/Nnf5tGhWdu9SRw7e/c922GZh9r5QCNVkmsUFyO8lItL4zvz8DBtFq0qRLTJmEhaAygATnff3wKajsHqwOJf3n9luTNnpGxonafX32cTNtrty3cPt0gBaHS5lOtM6aAEI9ogvuCNh4mDNWmisCUyO8Uly+Mbc4QNZNV6iyy/F1zs2dmD7dY2qT2Dvg8S/ut7LNnxeAc0uqTSIMqdhRNwAD4IA3HiDAMzSpi5hnpA4ZVxAXza77eXKCk0XbvXE8Nu5a122XfH2vlGhrZR+jWPb6u67f6rs3vZ4CQ/Oan+0r/Kf6RuI78zx++/k/9o3AO6XRIqkcWcLluQ4JGBtgYiPVqswTeBcOHfw2YPa9rPvtzhWtIWua9OFGWwzLe18vth4lhOk8C15fF4bM4uvt263P73gGNToUUqOLJBSsEByScHfBxDOkJ7ZcZ/esa1sM7vtvsIG0WXMRNBnhQlsczHtflvh4I1/vlHZu8z3cL3Ndb79/GAH1LUV0yzJlEJQlmBD7hzk53MSc/SZUuWZ6QRMSm8Fz7TPs7bxmjzZSZSUzygTMuJhF25Z3ztEPSyZwmpUsTBLvclT22vzfDNAEaVVKq18KebkBJUAMZBA3HmYXq040agiQbUqFxfOducE64tC5YFOUqXcCRKZ2Yv7OWdvlCNBUlCVCpZKiru8XdmG13J4B6g0qVPlpnTASteVEEh+WwLbCIuj1WZPWmVNUDLU4UGAwATuNtoRqcqcqasyRMMsnulD27DZsbvAv0l69JlUvDklBmzu66GdKfrHGz+z/AIj0gKu9ItRTMnz56P1aSRLDvjZO/UZ81GOfrBZJQn6yu8r3wbWS3MqT1Nyv9/GAa1d85uQLDyEeVZSIZM0+CU/HJhrSZb0849X/ABh4q9QpX2lE/CFaOhqVZ6hX3mAEqJX6NJV0EE0eV/3kv5jH4CCTJekQP7P/AJ/CAKSYyZa/sLz5GCl6WLJ6knZYi5vo0qjUSE0s0uiVcANjgunIyzKb3RUWqU9q0zB9VXy/+R3voRVKC5slAUTNSlSbXd0u4DdQST/CIsRYmrq7GUiR3b3Kny7M2/mYJ07TZdTLE6aCVqdyCRsSBgY2AhnQCEBfae6S1vF97td7vlAWrypqpqlSAsyy1plvbsHZsbv74qN0+rTZkwSVqBQpVhDAY233g/VqZNIgTJAtUVWkkvggnY+IEE1k6SZKkoMszClkhJF1zcmy7xGaElSJhNRclFpAM12ucbXYdn+cARpMhNWlS5/eUk2ggthgeXiYCrdVmyJipMtQCEFkggHDA7nPOHtdSpa0mmdSbWPC2dzvbzZoktNnSUykiaZYmAd4LIufxfMA3X6XKp5ap0oELR7JJJ3LbHGxMB6TMNYpSZ/eCQ4bGduUC6XKmpmoVOCxLD3Fb27FnfG7RIa8QtKRTMpQPe4W7Nzt5QA+q1SqRYlSDakpCiDnJJG58AIOk6VKXKE9QPEKbyXPtM7s7b8oRoS0IlkVBSldxIE3dmDe1lnf5xFzpM4zipImcK9wQ9tr8uVre5oBn85aj7Y/yj+kZHV9qpftSfimMgIyhr00ieDNe57u7kMdsloDOkTCvtHd4d3F3za92zbtBMrTxWDjKUUH2WGRjz84bOtKB7NYGfhXPlvZduvOAfrq9NWngynuJfvYDDfOYb05XYnE7PEa2zO27u3URubpwoxx0qKyO6xwM+Uakp7e5X3OHtbl7vPygGK3TV1SjOlW2KwLixwGOGPMQdN1iXMQZCQq9QsDjDs277PAs3VDSHgJSFhObiWJfP4w6rREyh2gLJKRxLWDE7tAM0FIqjVxZzWkW90uXLHm2MGN6hJNaQuSwCRab8Z3wzxuVWGtPBUAgDvuM7YbP8UbnTzQnhpF9/edWG5cvKAfpNXl06BJmBV6MG0OOuC46xxvpF6BzlFU8zEi1I7uSMeLdTHXS9FFSOOpZSZmSkBwOX4Q0jWVVJEhSQkLxcDkNn8ICja2nXJXMXNSUqPdQD06g7HriIWn2WroD8THojVNLl06CFoTPRM7qkTEgp67Zjz1r9NL7RPsSEpE1bJAwkXFgObAYiKXWC2SlP8AZf45/GCtOS1Kf4T+MCej8lU+qp5Kjclc1IUDnugurfPsgxfNf6FUkiUpZlIWkMLLQkF8fVY4d4CnpCfUpEQcqTmbL8HHuixfT7R5P5OmzaeVwVSlyybVrNwWuxu8rAcjaKpkSldSPImA7Gj0+ZUpSiWgqUe7jrsHOw35xYfox6GzaJcmpq1gcP6so3EkgjJIGGJdvjCfoq0Yfk5M+4uha1BLYNpf5kR1cqvNaeCoBA9pxnbz84cOt6ik1pSZOOG4VfjdmZn6GHqPVEUqBIm3Xpd7Q4yXDFxyMMzldgYI7/EybsNb5ecLl6SKscdSigr+qA4Dd38IqBZWjzJSxPVbYk3ljlt8Bt4L1CqTWJEqS4UDeb8BgCOT5dQhka2qaezlAAX3Lgcjk8OTqQUI4qTeVdxlY3y+P4fnAZp9QKIGXOcqUbhZkMzc26QNVaRMqFmdLtsXkXFj0yGPSCJNP255ijYUd1k5fm+fOEL1pVMTICAoIwFEsTz/ABgCavV5dSgyJYVevAuDDGclzyED6fKNESudkLFosznfLtDkzRhSjjpUVFGQkhgXx+MIkzjXGxYss7wKcu+OcAivpFViuLJa0C03li4c8nxkQVL1iWhApyFXpTwywxczbvs8Dzaw0J4KQFg99zjfDY/hhxOipWO0lZBUOJa2H9pn6QEf+as/rL/zH+kZBH53L/dJ/wAx/pGQCNYq100zhSVWIYFsHJd93MSg02UZPGt9ZZxLnPtNc7O2+YRotQiTLsnkIW5LK3Y7REqo5vG4lquHxLruVtzv5NmAd0esXUzBKnKvQxNuBkbbAGH9dPZSgSPV3vdzdmb2n6mCtaqUTpRRIUFrcFk7sN4H0A8C/tHcua27mzu3xEATpNDLqJSZs5N61O6nIdiQMAgbCIim1KauaJKlvLUq0pYZS7M7PtCtYpZk6aqZJSVIIDFO2Ax+cTNVXSlSVS0qSZhRaEje5mbzeAH1umRTSxMkCxZUEk74IJbLjkIRocoVSVKn+sUksDswYHk0DaHJVImFc8FCLSAVbO4x8AYXryDPWlVOCtIDEp5F/wCkALqWpTZM1UqUu1CSyUsC2AeYJ3MTOpadKkylTZabVpDhTktltiW5xvTK2VLlJlzVJStI7wVuMneIXTaObLmpmTUqShJdRVsMHf3wBWhzTVKUmeeIlIcDZi7cmin/AE60US62pQAw4jjyUAr/AKoufXpgnpSmnN6gpyE8gxim/T2s4NStCgQsJTc/IkYHwt+MSrCvot0ZK9RBUH4UqYv3sEg/zRamk18yomplTV3oU7pYB2BIyADuBFV/RbP4lUsJcr4dzD7IWm4nydMXTq9XLmylS5KgqYWYJ3LEEt7nhEc99JdAmXQzpcpNomS13De60BSd3Zj0ii6SQwA5xeWoVyaGjqptUlQBl2pSd1EulhvzUPjFG0NelWN/Lf4QqxbvoBVTEU0iQFNLUsgpYZCphfLPkGO11mkRTS+JJFi3AucnB33cRFehmpSFaZJQkgTOEUWne91AgeatvMQ7oshcmZfPBQhiHVs5ZoqCdCAqgsz/AFljW8md39luggPVa+ZTzVSpS7EJZksCzgE7gncmCtf9eUGn74SDdZydmf4GDNIq5cqUmXOUEzA7hW4ckh/cRAKq9MlS5SpqENMSm4Kc4LO7EtEbolQqqmGXPVegJKgMDIID4Y7E/GB6WimompmLSoSwq4qOwD7+USeuzkz5YRIIWsKBITuzEP5ORAC65ONKsIkHhpUm4jdy7Pl+QiR07TZU6UmbMTctQdSnIf3AtA2gzEyEKTUEIUVOArow/F4jtSo5syatcpKlIUXSRsQw2gN6ZqMyfNRKmruQp7ksA7AncAHcCD9clilSlUj1ZUWJ3cM/N4K1StlTZSpcpSVLU1oTucg49zxH6CkyFKNR3AQySrmX5QBOiU6amWZk8XrCikHbAALYYbkxGTtSmpnGSlbSwuwJYey7M7PtD+uSlT5gXIBWgJAJTs7nHmxESlPWykyRLUpImBFpTzuZm83gH/zepv3X8yv6xkcj+San92v/AH74yAlqmgVWK40shKWtZW+PJ+sEfllAT2a1V7cJ8M/svu7PA9TXqo1cGWApLXOrfPkw5QR+RkFPablXtxWwz+02zs/jAD02nqo1caYQpIwyd8+bCF1Y7cxld3h7387tmZ+kJptQVWK4MwBKTl07483EKqz2FhK73E3v5W7MzdYByn1RNIngTApSk5JSzZzhyDzgdGirlqFQVJKUm8gO7btszwRT6WmrTx1qUlSsEJZsYw4J5QOjWVzFCnKUhKjYSHdtnGWeAfqqwVo4MsFKgb3XswxyfOY1STxQgomAqKzcLOm2XaN1VGKIcaWSpRNjL2Y55NnEapJArgVzCUlBtAR03y7wDM/Rl1KjPQpISvICnccssCOUEz9ZRUpMhCVBS8AqZg2csX5QNO1ldMoyEJSUowCp3PPLEDnBM/RkUyTPQpRUjICmYvjLB+cAzSyDQkrmd4L7oCPjl2ip/pNnS11U1RlrtnBK0qIdiEhBGH+yD/ii2KWea4lEzuhHeBR12y7wFrtJJlNIXJlz0teDNS5STg2kM3sjaApD0J1g0NWmexsYy5gtZ0KZyAByYK8Snxi+abS1UpFQtSVJTyS7l8BnAHOBab0Fo1S0zrCCUhdjkpHNmL48IKp9VXVqEhYSlK9yl3x3sOSNxAcN9M3pEmfJlSJaFAklSipLsAzeyd3+6K100S0JJKVqX0Ys/iYvzXdIkSAkKlInhR2nJCrW5pwGJeE6b6I0lQgTDKSjJFiEi3HNiDmIoH0S0BaKSmnukJ4aJxTl8gLI2Z8mJ6qr01g4MsFKvadTMw8nPOBxrC7uzWpsfhPl2e192dvCCKmgTRjjSyVK9llM2fIA8oqE0i+wuJve4mRZybd3brDVRpS6tRnoKUpXsFO4bu5YEbiHaRHbnM3u8PAs5vu7v0hqo1VdIoyEBKko2KnfPeyxA3MATN1pE1Jp0pUFLFgJZn2zl2hilpDRHizCFAixkbuc82x3TD83RESkmoSpRUgXgFmffOHaGKWrNarhTAEhIvdG7jHN8d4wG6qQa48SWyQnukL3ffDP1h6RrSKdIkLSoqRglLN1w5fnDNVPNCeHLZQV3iV7vthm6Q9J0VFQkT1qUFLyQlmHLDgnlADSNHXTKE9akqSjJCXcvjDgDnD1XOFcAiWLSjvG/wAcYZ4ZkawupUJCwlKV4JS7hs4ckcodq5IoWXLJUV903+GcM0AqlrBRDgzAVKJvdGzHHNs92B1aMtau0BSQlR4jF3Z7m2Z2giloxWjizCUqBsZGzDPN896B1aytCuzhKSlJ4YJd2e192doA387ZX2JnwT/3RkZ+aUr7cz+X/tjICJ9Lf2g/wj8Y6ZP7J/8Aj/0RkZAcz6J/tA/hMG+mu8ryV/0xkZASnot+zI81f6jHK6d+1I/vPxjcZAdF6Y/qE/xj7lQ16F/q5n8f4CMjICC9If2ib5j7hHW+kP7NM8h94jIyAg/Qz9Yv+H8Yb9Mf16f7sfeqMjIDodP/AGVH90P9Mcr6L/tMv/F/pMajICW9NdpXmr8IN9E/2cfxK++MjIDmf/6//wB/+uOl9Lv2f/EPxjIyAD9Ctpvmn/qiJ9KP2mZ/h/0pjIyA6zVP2Zf92fujnfQ79er+7P8AqTGRkAv0z/Wo/g/ExPaB+zS/4fxMZGQHJejn7TK8z/pMTfpp7Ev+I/dGRkA96HfqFfxn7kxztX+1K/vv+qMjIDv4yMjID//Z	Vane Web	f	2024-08-26 10:24:22.242713	1
1	2024-07-17 22:29:51.800891	Herramienta de desarrollo para manejo de tareas y administración y desarrollo de proyectos.  La app está pensada para ser usado para todo tipo de proyectos y ser bastante customizable para que se adapte al flujo de trabajo de los usuarios y de las necesidades del proyecto y no al revés.\n\nTarjetas con información sobre distintas tareas, vistas para organizar las tareas, la posibilidad de invitar usuarios y crear/manejar equipos.	https://thumbsnap.com/i/7QHiR6fM.png?0821	Dev-cards	f	2024-08-26 10:45:07.902967	1
16	2024-08-17 19:35:16.393977	El viejo "Ideas para escribir.doc" o el 'TO DO.txt' versión tarjetas. 	https://w7.pngwing.com/pngs/203/179/png-transparent-graphic-designer-logo-innovative-ideas-web-design-hand-orange-thumbnail.png	Ideas	f	2024-08-21 12:11:29.078938	1
\.


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 216
-- Name: tasks_task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tasks_task_id_seq', 82, true);


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 221
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 8, true);


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 227
-- Name: workspaces_workspace_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.workspaces_workspace_id_seq', 21, true);


--
-- TOC entry 3235 (class 2606 OID 119217)
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (task_id);


--
-- TOC entry 3237 (class 2606 OID 125583)
-- Name: tasks_progress_items tasks_progress_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks_progress_items
    ADD CONSTRAINT tasks_progress_items_pkey PRIMARY KEY (issue_id);


--
-- TOC entry 3241 (class 2606 OID 119259)
-- Name: users_designated_tasks uk719lt3i9stmub1uxtvx7kfxp8; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_designated_tasks
    ADD CONSTRAINT uk719lt3i9stmub1uxtvx7kfxp8 UNIQUE (designated_tasks_task_id);


--
-- TOC entry 3239 (class 2606 OID 119235)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3243 (class 2606 OID 119255)
-- Name: workspaces workspaces_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspaces
    ADD CONSTRAINT workspaces_pkey PRIMARY KEY (workspace_id);


--
-- TOC entry 3255 (class 2606 OID 119327)
-- Name: users_workspaces fk2uww6j6j5u9em7ds9awuwbame; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_workspaces
    ADD CONSTRAINT fk2uww6j6j5u9em7ds9awuwbame FOREIGN KEY (workspace_id) REFERENCES public.workspaces(workspace_id);


--
-- TOC entry 3262 (class 2606 OID 133829)
-- Name: task_user fk32eeu8p13crqmo7dfdtn6hncm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_user
    ADD CONSTRAINT fk32eeu8p13crqmo7dfdtn6hncm FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3259 (class 2606 OID 119347)
-- Name: workspace_users fk48lhf8wl0kb2oxupyil6j2mkr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspace_users
    ADD CONSTRAINT fk48lhf8wl0kb2oxupyil6j2mkr FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3256 (class 2606 OID 119332)
-- Name: users_workspaces fk5h20nxbt4grbqtnqgxaap5whg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_workspaces
    ADD CONSTRAINT fk5h20nxbt4grbqtnqgxaap5whg FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3246 (class 2606 OID 119282)
-- Name: tasks fk6s1ob9k4ihi75xbxe2w0ylsdh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk6s1ob9k4ihi75xbxe2w0ylsdh FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3245 (class 2606 OID 119277)
-- Name: task_entity_task_tags fk92l5keg2joseig4j1yd8dqgr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_entity_task_tags
    ADD CONSTRAINT fk92l5keg2joseig4j1yd8dqgr FOREIGN KEY (task_entity_task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3264 (class 2606 OID 142158)
-- Name: workspace_collaborators fkbwrt0blhw5g1fx3jokxghwfse; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspace_collaborators
    ADD CONSTRAINT fkbwrt0blhw5g1fx3jokxghwfse FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3247 (class 2606 OID 119292)
-- Name: tasks fkc9qtufdiesth3hltky4dnacje; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fkc9qtufdiesth3hltky4dnacje FOREIGN KEY (workspace_id) REFERENCES public.workspaces(workspace_id);


--
-- TOC entry 3265 (class 2606 OID 142163)
-- Name: workspace_collaborators fkd7eu848ldmvah910c87bdwure; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspace_collaborators
    ADD CONSTRAINT fkd7eu848ldmvah910c87bdwure FOREIGN KEY (workspace_id) REFERENCES public.workspaces(workspace_id);


--
-- TOC entry 3260 (class 2606 OID 119352)
-- Name: workspace_users fke9fam5153pf248i87es7xt94j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspace_users
    ADD CONSTRAINT fke9fam5153pf248i87es7xt94j FOREIGN KEY (workspace_id) REFERENCES public.workspaces(workspace_id);


--
-- TOC entry 3261 (class 2606 OID 119357)
-- Name: workspaces fkfwu53godully4c0ppjg0hhy3s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspaces
    ADD CONSTRAINT fkfwu53godully4c0ppjg0hhy3s FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3263 (class 2606 OID 133824)
-- Name: task_user fkgaja271i7nlmd3qhg85coclr7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_user
    ADD CONSTRAINT fkgaja271i7nlmd3qhg85coclr7 FOREIGN KEY (task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3249 (class 2606 OID 119297)
-- Name: tasks_dependencies fkgdm66lh3frfa9db4htokkn6kr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks_dependencies
    ADD CONSTRAINT fkgdm66lh3frfa9db4htokkn6kr FOREIGN KEY (dependencies_task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3248 (class 2606 OID 119287)
-- Name: tasks fkh279lo9lqbhxqh68jq9sqs83s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fkh279lo9lqbhxqh68jq9sqs83s FOREIGN KEY (owner_id) REFERENCES public.users(user_id);


--
-- TOC entry 3252 (class 2606 OID 119312)
-- Name: user_roles fkhfh9dx7w3ubf1co1vdev94g3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3251 (class 2606 OID 119307)
-- Name: tasks_progress_items fkhh3kpajpfs7ncgj3ufu2icjpb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks_progress_items
    ADD CONSTRAINT fkhh3kpajpfs7ncgj3ufu2icjpb FOREIGN KEY (task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3253 (class 2606 OID 119317)
-- Name: users_designated_tasks fkl44div94y2olr3b9nkqh2vb0l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_designated_tasks
    ADD CONSTRAINT fkl44div94y2olr3b9nkqh2vb0l FOREIGN KEY (designated_tasks_task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3254 (class 2606 OID 119322)
-- Name: users_designated_tasks fkl7xtf0syrjgf3pwj23agc1b6d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_designated_tasks
    ADD CONSTRAINT fkl7xtf0syrjgf3pwj23agc1b6d FOREIGN KEY (user_entity_user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3250 (class 2606 OID 119302)
-- Name: tasks_dependencies fkn9tofjx9bf06yn8fp2g9480bs; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks_dependencies
    ADD CONSTRAINT fkn9tofjx9bf06yn8fp2g9480bs FOREIGN KEY (task_entity_task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3244 (class 2606 OID 119262)
-- Name: task_updates fkntllp5kf24w3bm2ldtfsh64n9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_updates
    ADD CONSTRAINT fkntllp5kf24w3bm2ldtfsh64n9 FOREIGN KEY (task_id) REFERENCES public.tasks(task_id);


--
-- TOC entry 3257 (class 2606 OID 119342)
-- Name: workspace_moderators fkq7f5wryuks3ymkq9ocfs8buje; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspace_moderators
    ADD CONSTRAINT fkq7f5wryuks3ymkq9ocfs8buje FOREIGN KEY (workspace_id) REFERENCES public.workspaces(workspace_id);


--
-- TOC entry 3258 (class 2606 OID 119337)
-- Name: workspace_moderators fkt3kmbwn1rnelaw0r8d5owjdsf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workspace_moderators
    ADD CONSTRAINT fkt3kmbwn1rnelaw0r8d5owjdsf FOREIGN KEY (user_id) REFERENCES public.users(user_id);


-- Completed on 2024-08-30 13:38:31

--
-- PostgreSQL database dump complete
--

