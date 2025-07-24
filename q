[33m3bda669[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mdev-dbchanges[m[33m, [m[1;31morigin/dev-dbchanges[m[33m)[m changed env app/prop. They were placeholder anyway
[33mc701ee7[m fixed some client's bug related to tasks_id changes. DB 'migration' completed!
[33m3cdad05[m fixed more type changes
[33mc0fd71f[m refactor task_id type from Long to UUID
[33md40e2ea[m refactor all codebase to change task_id type from Long/number to UUID/string
[33m205bd52[m[33m ([m[1;31morigin/dev[m[33m, [m[1;32mdev[m[33m)[m Task Entity needs changes (Long ids to UUID ids). Preparing branch for this.
[33ma7bb493[m added TaskWithReference/TaskDTOWIthReference types as an object with two references: the imported task and a TaskSlim/TaskMini with a reference to the pre-imported task
[33m9721cce[m fixed import workspace response wrong type.
[33mbd85a25[m added visual feedback for the import process. Added client connection to the new endpoint. The import process is a WIP.
[33md4d6d8f[m added a new endpoint to the API for tasks's import and related methods/services. Fixed tasks dependencies's disgregation in import process not working at client side
[33md13a345[m changed special tags bg color to a gradient. Added tailwindcss's safelist color patterns
[33m8ce2fcb[m fixed a type error in ImportWorkspace component
[33mb4852a5[m commented uncompleted code
[33m30a2ba4[m added ImportWorkspace component. Added first business logic aproximation from client side to import a JSON into the db via the API
[33mb0ee8aa[m added touch start/end event in lateral menu component
[33m23a3c11[m added more logic to import jsonws
[33md045ac0[m added more options to track local/online exportation
[33m013ade0[m added onlineExport and localExport properties to JSONWorkspace model
[33mcf5d77a[m fixed optional type for workspace_id errors
[33m92e1f95[m added Importing JSON component and added logic to import workspace and configurations. Task logic WIP
[33m977fc08[m fixed some special tags navigation bugs <WIP>
[33m8f987ef[m added SpecialTagNavigation component. Added the ability to get all tags with special tag on click.
[33mdfa39fc[m added hover effect to SpecialTagElement to show diferent background on add/remove in AddSpecialTagModal
[33m130a071[m added AddSpecialTagModal component to the Task Control's side menu. Users can easyly add or remove Special Tags.
[33md25048c[m added SpecialTagElement component, added Special Tag visualization in TheTask component. Fixed tag parsing in task utils. Moved the logic to transform tags in UITags from THeTask to the TagList component. Added a color selector in SpecialTagEditorModal to select a color to use for Special Tags -which will be saved in special tag's value string
[33ma4fd696[m fixed SpecialTag's delete and update operation in API. Fixed Special Tag's delete modal not closing in client's online mode
[33mb82f516[m removed useless console.log lines
[33m8e8b217[m added the ability to delete special tag. Added API's connection to special tag's crud operations
[33m7c4f442[m added Special Tag Editor Modal implementation -user can add and updated special tags-. Added special tag parser. Fixed Custom Glosary update creating a new glosary.
[33m635b0cc[m added implementation to show/hide Special Tag Editor Modal in Special Tag Control
[33mcb61ba3[m added basic implementation of Special Tag Editor Modal
[33m842e314[m added basic implemention to Special Tag control
[33m92fa7f7[m added the null implementation to priority selector. Fixed Custom Glosaries list not updating after crud operation. Fixed workspace not updating after custom glosary's CRUD operations
[33ma0c26d2[m added the ability to null glosarie items values. Fixed task's states showing null values
[33m3b791f7[m added custom glosaries label to CommonSelectable task's state inputs
[33m2769111[m changed newProject width componenent to fresh users
[33mb87d495[m added Custom Glosary logic into TaskCommonSelectable component
[33mbe34f25[m added getCustomGlosaries method in task utils. Changed TaskPrioritySelectable to check enabled custom glosary to show custom values
[33m085aafe[m fixed udpating/deleting customGlosaries in API not working
[33m48263f3[m fixed client not refreshing configs after a change of workspace
[33m0d8e0b7[m reverted java version to 21. Fixed null custom configuration in old projects, whenever the user fetch a null custom configuration the api will create a new one and save it to the db.
[33m413db60[m changed addNewGlosary component to support to edit glosaries. Added dinamic visua feedback about which glosaries could the user create. Added Remove Custom Glosary implementation.
[33m89bae9e[m reverted update to DaisyUI and Tailwindcss. Added new glosary functionality and added the ability to remove a custom glosary in project settings
[33m1eada00[m added dinamic form to new glosary modal component. Updated DaisyUI
[33m8479bc7[m added addNewGlosaryModal component and logic -wip-
[33m51f46bc[m fixed TheTask component not rendering after move  'updaes' outside of the main template element's component
[33mb45b93a[m added task-update creator link to profile in TaskUpdate's creator username
[33m22f1a8f[m changed text in login component
[33m6fb1685[m changed task updates/comment style to a separate card in TheTask component
[33mdde8519[m fixed notebook's screen design for project lateral menu, newtaskbtn, changecardsizebtn, changecardsizebtn, taskfilterinput, scrumview and theproject
[33mcead04b[m fixed create new project dont adding a new configuration in the API
[33m9eafc86[m fixed adding task update in local model doens't adding creator id and username to task update entity
[33mac106c5[m changed 'offline mode' to 'local mode' to avoid confusion -the web is hosted online, there is no local client yet
[33m1aebcb5[m fixed newProject component buttons alignement in notebook's screens. Fixed Createt New Task button size in scrum board for notebook's screens. Fixed New Task Modal design to notebook's size screens. Fixed AppSettings height in notebook's screens. Fixed LoadWorkspaceBtn component size in notebook's screens.
[33mded3c4c[m fixed project lateral menu sizes, height, fonts, width, layout, etc, in notebook screens (min 1024px width)
[33me11f193[m fixed create new project component using wrong margins after sign up as a new local user
[33mad7c08b[m added getActualDate method in date utils. Fixed saving JSON file name -now it will used the actual Date to saved it-. Fixed TheTask width in 2xl screens.
[33m5409ed6[m fixed localStorage bugs when creating and saving projects, users and tasks
[33m9303522[m fixed responsive design for small screens to TheTask and child components
[33mee4ea63[m fixed summary inside a div error in ProjectLateralMenu component
[33m49a8a18[m fixed responsive design to offline signup, app-lateral bar height and homelayout top menu options
[33m24b4bc7[m fixed responsive designs in many components
[33ma49e9e4[m added generic user and project avatars for default. Fixed create new project component margins.
[33me1aca73[m fixed project lists not updating after creating or deleting a workspace
[33m5d1822b[m another fix to the offline mode freeze in production
[33m30651eb[m firebase configuration
[33ma85c2f9[m changed offline mode user creation flow. Changed getRandomID to pick 1 or id+1. Fixed web not rerouting to login page in first load
[33m95f68d4[m hardcoded local id to 1
[33mb3ed941[m linter
[33mbf9a230[m Merge branch 'dev-testing' into dev
[33mc901693[m limited id generation to 100k
[33m2aec113[m Merge branch 'main' into dev-testing
[33mde40cc8[m Merge pull request #72 from Ltizzi/dev
[33m0861a64[m fixed task description type bug. Added new web build
[33m10786f0[m added debug console lines to follow deployed local user creation problems
[33md1b6d97[m changed max randomID number to 1m
[33m409e6d7[m Force fixed loading spinner on first load
[33md44e07c[m fixed modal always loading bug..for real
[33m682317e[m fixed modal always loading bug
[33m8445fbd[m added new web client build
[33me0a00c7[m Merge pull request #71 from Ltizzi/dev-testing
[33m8dd3c6e[m Merge pull request #70 from Ltizzi/dev
[33m0b679dd[m fixed save to JSONWorkspace from server not working. Now the users can save a workspace form the server and then load it in a local enviroment
[33ma0a9c6c[m Merge pull request #69 from Ltizzi/dev
[33m5dc5b0d[m added load/save JSONWorkspace implementation for local workflow
[33m7359db1[m Merge pull request #68 from Ltizzi/dev
[33m6038f90[m fixed uistore looking for a localstorage item in first load -there is nothing there-
[33m278f8c9[m adding main repo folder an gitignore
[33m6f6dcca[m Merge pull request #67 from Ltizzi/dev
[33m99c6393[m added load JSON btn
[33m44fe5ab[m Merge pull request #66 from Ltizzi/dev
[33m6cbf5cb[m changed btns size in TaskListFilters component. Added AuthUtils mappers in 'getSelfAsUserLite' user store method
[33me672001[m Merge pull request #65 from Ltizzi/dev
[33md5b5416[m added loading spinner component
[33ma53d1ff[m Merge pull request #64 from Ltizzi/dev
[33mc20db29[m fixed project card z-index on hover (its stand behind the no hovered cards)
[33m48ff629[m changed margin-x values in project info. Fixed overflow in x axis in scrum view
[33m6c76f03[m Merge pull request #63 from Ltizzi/dev
[33m786e041[m fixed manual refresh blocking the ability to create new task to logged users -online mode-
[33m0471244[m Merge pull request #62 from Ltizzi/dev
[33m31bc832[m cleaned code comments
[33m03e77b3[m Merge pull request #61 from Ltizzi/dev
[33m0907784[m fixed custom configuration duplicating saved tags. Added a temporal fix to avoid showing tag duplicates in client's components
[33m0658a4a[m fixed task's animation affecting task's control side menu too
[33mc7deea9[m Merge pull request #60 from Ltizzi/dev
[33m9f08f1b[m fixed taskminicard dont changing z-index on hover. Added animations to tagnavigationpanel and thetask components.
[33m1b5e931[m Merge pull request #59 from Ltizzi/dev
[33m86a6497[m changed base modal animatino. Added animation to login, project and app settings.
[33m70e1b8e[m Merge pull request #57 from Ltizzi/dev
[33m7239c67[m added animations to base modal, to project lateral menu and userProfileView
[33m6e0742e[m Merge pull request #56 from Ltizzi/exp-motion-anim
[33m2ae524e[m added motion tailwind animations and animated project cards and task mini cards
[33med762a7[m Merge pull request #55 from Ltizzi/dev
[33m1c92a6b[m cleaned code. Added skeleton methods to remove special tag (and TODO about its implementation)
[33m6784888[m Merge pull request #54 from Ltizzi/dev
[33m4eda331[m added TagsList component with the objective to be an reusable component to use it for normal and special tags (and to clear some of the boiler plate code in The Task component
[33m5b40537[m Merge pull request #53 from Ltizzi/dev
[33m6b2e708[m fixed task priority dont updating
[33mb01bae8[m Merge pull request #52 from Ltizzi/dev
[33m7940f9c[m changed how tags render in theTask component.
[33m8220046[m Merge pull request #51 from Ltizzi/dev
[33me9d9b8b[m added SpecialTagsControl component to ProjectSettings. Added tag parser methods (to split between normal and special tags)
[33m72df71e[m Merge pull request #50 from Ltizzi/dev
[33m8a8ede5[m added GLosary control in Project Settings.
[33m2387ba2[m Merge pull request #49 from Ltizzi/dev
[33mb5de497[m changed task filter by tags to avoid using .includes method.
[33m79fdee7[m Merge pull request #48 from Ltizzi/dev
[33maf08586[m added needOnline prop in MenuOptionUI to limit menu visibility in project settings to hide moderations settings if user is in offline mode
[33m69b5eba[m Merge pull request #47 from Ltizzi/dev
[33m735ee2b[m fixed update description/task not working in local mode.
[33m13989c6[m Merge pull request #46 from Ltizzi/dev
[33m851a3b8[m fixed autoassing task in offline mode do not update taskLite array in workspace entity
[33m7815975[m Merge pull request #45 from Ltizzi/dev
[33m3d79450[m fixed TagNavigationPanel not refreshing tags after switching from online mode to offline mode -it keep showing online workspace's tags-
[33m97a0e86[m Merge pull request #44 from Ltizzi/dev
[33mf18e3a5[m fixed online mode not using CustomConfiguration's tagPool.
[33mda19570[m Merge pull request #43 from Ltizzi/dev
[33m60eb1de[m fixed remove tag in local. Now the client will delete the tag in the configuration tag pool if there isnt tasks with target tag
[33ma0a5eda[m Merge pull request #42 from Ltizzi/dev
[33mda758e8[m fixed add tag not working in offline mode. Removed assign/unassign users in task's side contorl menu.
[33m41cbdca[m Merge pull request #41 from Ltizzi/dev
[33mf1c9810[m fixed task's updates dates don't getting saved in local mode
[33m529433e[m Merge pull request #40 from Ltizzi/dev
[33m9ca8c37[m fixed create workspace/create task bugs in offline mode.
[33m8311b06[m Merge pull request #39 from Ltizzi/dev
[33m8b4ad2f[m Merge pull request #38 from Ltizzi/dev
[33mcf8b08c[m fixed projectList doesnt rendering
[33m3b22227[m removed local prop in project store, change to use current instead.
[33md7795cd[m Merge pull request #37 from Ltizzi/dev
[33m8f871f6[m fixed more refactor bugs -now in the online side-
[33mc588d05[m Merge pull request #36 from Ltizzi/dev
[33m1ce6a65[m fixed several stores/states errors related to projectStore.current prop. Users now can make tasks in offline mode. Users now can normal-ish navigate inside workspaces.
[33mef8b98a[m Merge pull request #35 from Ltizzi/dev
[33m365c0ee[m fixed offline mode dont persist after manual page refresh
[33md07ebcb[m Merge pull request #34 from Ltizzi/dev
[33ma5a2047[m fixed severals bugs using offline mode. Added UserWorkspaceRoles local implementation -more development required-.
[33m70558d0[m Merge pull request #33 from Ltizzi/dev
[33mbcb0bf8[m added offline signup component.
[33m0c4164a[m Merge pull request #32 from Ltizzi/dev
[33m3727b9d[m added id generator in utils. Added offline mode methods in all client's stores. Added classes mappers in client. Added utility interfaces needed for local workflow
[33mcee667d[m Merge pull request #31 from Ltizzi/dev
[33m827f327[m updated JSONWorkspace class to include CustomConfiguration. Added JSWONWorkpspace interface in client. Added offlineMode prop in UI store.
[33mf8eab0e[m Merge pull request #30 from Ltizzi/dev
[33mfb6217b[m added more utility methods in config store (get tags/special tags/glosaries/glosary by type/flagged tasks). Fixed fetch config by workspace_id query in CustomConfigurationRepository. Fixed Home component rendering twice in client app.
[33ma7d107c[m Merge pull request #29 from Ltizzi/dev
[33mcfabcb0[m added config store to client with all avaible api calls methods.
[33m224a35e[m Merge pull request #28 from Ltizzi/dev
[33ma109acb[m removed themedata props from CustomConfiguration. I realized than CG is a workspace related class. Theme data should be something asociated with user data.
[33mc178bab[m Merge pull request #27 from Ltizzi/dev
[33m0876ee9[m fixed used profile component not working.
[33m62573ae[m Merge pull request #26 from Ltizzi/dev
[33m5033d8b[m added THemeData class and ThemeData prop in CUstomConfiguration. Added simple tags crud methods. Fixed ProjectList don't rendering and crashing web client app. Added configuration endpoints to Endpoint enum.
[33me47e5a9[m Merge pull request #25 from Ltizzi/dev
[33m36c9355[m moved apicalls from component to stores preparing client to refactor.
[33mc8c9997[m Merge pull request #24 from Ltizzi/dev
[33m16de957[m added TagPool and UITag -tag+color- classes. Removed SpecialTags as prop and moved into TagPool prop.
[33m5b7145f[m Merge pull request #23 from Ltizzi/dev
[33mb9b5029[m fixed theproject dont reloading after altf4 or loading the page in another tab
[33mb05a854[m Merge pull request #22 from Ltizzi/dev
[33mcf76d03[m added configuration dto to avoid data redundance in responses. Added configuration Mapper and updated return type in configuration service/controller. Added configuration related interfaces in client side.
[33mf601347[m Merge pull request #21 from Ltizzi/dev
[33mc863fa4[m added MiniTaskDTO class to handle CustomConfiguration tasks's lists. Added CustomConfiguration repository, services, controller.
[33m4ed2480[m Merge pull request #20 from Ltizzi/dev
[33m1336bea[m added CustomConfiguration class (with helper subclasses as CustomGlosary and Special Tags) to the API. This is the first step to add user customization to the app.
[33m6c1aa8e[m Merge pull request #19 from Ltizzi/dev
[33m870a267[m added updated Task model to client
[33maad7553[m added child_tasks prop to track whenever a task add a dependency (parent task) to add the first task as child. Updated taskDTOs and mappers
[33mc34a7b6[m Merge pull request #18 from Ltizzi/dev
[33m9cbd4b3[m polished projectlist styles to adjust 5 projects per line en 1920px width screens. Added hover animation to project cards
[33m1009d39[m Merge pull request #17 from Ltizzi/dev
[33m0e2ee4d[m fixed some responsive design related visual bugs
[33mfe1babb[m Merge pull request #16 from Ltizzi/dev
[33m1b1d85c[m added better responsive design for window.innerWidth<1600 screens
[33m6d636a1[m Merge pull request #15 from Ltizzi/dev
[33m5c78e15[m adjust responsive design in TheTask. Added noteebook responsive design in TheProject, AllTaskView, PRojectInfo
[33m5eb3e8a[m Merge pull request #14 from Ltizzi/dev
[33m9c60d09[m fixed hide project lateral menu btn showing in large screens
[33mf597312[m Merge pull request #13 from Ltizzi/dev
[33me99edfe[m added responsive design for small screens -phones- in almost all the components. Scrum view need more work.
[33m29a6068[m Merge pull request #12 from Ltizzi/dev
[33me6bae1a[m added menu-hide and menu-show buttons to project lateral menu. Added mobile screen checker. Added responsive design to Project Info and AllTask -and subelements-
[33m6bbe019[m Update README.md
[33m9d8fb5c[m Merge pull request #11 from Ltizzi/dev-testing
[33m98651c0[m changed Readme.md, added 'web-install' script to package.json
[33mb44628d[m Merge pull request #10 from Ltizzi/dev
[33m3d50bea[m added BaseUserAvatarItem to TheTask designated user's list
[33m013ae2d[m fixed designated tasks no tasks text inside projects. Changed 'Issues' title in THeTask to hide if there is no issues and show 'add Issue' instead
[33m6d2422a[m Merge pull request #9 from Ltizzi/testing
[33mb0f904e[m Merge branch 'dev' into testing
[33m46a70b5[m root directory changes
[33mf4978ac[m reverted deploy env configuration
[33m0eb9bdd[m[33m ([m[1;31morigin/prod-testing[m[33m)[m Merge pull request #8 from Ltizzi/testing
[33m1e717ca[m reverted client env changes -was not working in prod-
[33m80fd3a0[m Merge pull request #7 from Ltizzi/testing
[33m97aac4c[m change .env to only one url
[33me396926[m Merge pull request #6 from Ltizzi/testing
[33meb4a5a7[m added client env
[33me0fab0b[m Merge pull request #5 from Ltizzi/testing
[33mdbe73d4[m fixed can't check other users profiles
[33m953b478[m Merge pull request #4 from Ltizzi/testing
[33m0005114[m btw I forget to copy the jar file into docker folder xD
[33m0d80ede[m[33m ([m[1;33mtag: [m[1;33m0.0.1a[m[33m)[m Merge pull request #3 from Ltizzi/testing
[33m7c8d051[m testing another hotfix, same issue
[33m6e863b0[m Merge pull request #2 from Ltizzi/testing
[33m3cf554a[m fixed sameGroup jwtUtils checker
[33mf9084d9[m Merge pull request #1 from Ltizzi/testing
[33m64feb6f[m fixed user don't being added to public dev-cards workspace
[33m2bf63e8[m opened branch for testing production fixes
[33m833234c[m new branch for development
[33m6accbd5[m fixed no tags in local causing errors
[33m1b5d814[m fixed public workspace ID
[33md79da42[m new users are added to dev-cards-public group.
[33m4670f1d[m fixed api url
[33m82c6765[m changed API_URL constant name to just URL
[33m525bfe7[m added API_URL constante to main.ts in client
[33m4fe505a[m added api url
[33mc1a62d0[m changed frontend url to enable cors
[33m4cc0fbf[m adding dist folder to repo
[33m74763b1[m preparing project to deploy
[33m4f182e3[m changed component title in AppSettings to App Setting -its was Projectt Settings because I copypasted it from the other settings layout. Fixed Projects continue to display in home layout after logout
[33m5705228[m limited scrum board height. Added links in TheTask tags to AllTaskView with Tag filter
[33m8d9a4e1[m added 'baseUserAvatarItem', a simple reutilizable component that displays the user's avatar and username, with optional link to profile.
[33m8e4a6cc[m fixed a bug when a tag was adding twice to tagpool
[33m4b0a5e8[m removed tagNavigation filter logic from AllTaskView component and moved to TaskListFilters. Now tag filter are fully integrated with the other filters and search values!.
[33m6bd005b[m added tag pool control btns into FilterTaskList component. Fixed Tags not showing in ProjectInfo after change tag pool visibility in AllTasks component
[33mdc28f0e[m added TagNavigationPanel. Added it in AllTaskViews and ProjectInfo.
[33m3bde082[m added projectCard and ProjectList components. Added UITag class helper to handle tags colors/names.
[33m282168d[m enabled TheTask watchers for route.query.id. Polished more styles.
[33mb04fb02[m fixed a weird bug -app crashed after re-enter a recently updated tasks-
[33mab35d06[m polished styles all across the app
[33m13c1c8b[m added user profile setup to change user's props. Added user profile view component. Fixed project lateral menu navigation's feedback overlaping. Added a new checker in jwtutils in the api to check if user checking other user's profile is in the same workspace than target user.
[33maf19dd3[m added appSettings and Theme control components. Splitted ThemeSelector component in a dropdown and a options list.
[33mcf3b925[m added reusable BaseToggle element. Changed Change-theme and darker cards toggle in HomeLayout to use BaseToggle component. Added 'darker mini cards' toggle to separate the dark theme of the TheTask component and the MiniCard styles.
[33mb46413b[m fixed min-heigh in some components. Fixed flagAsNew user to use created_at prop/use a localStorage variable instead of a setTimeout.
[33m2396263[m added roboto font. Changed Workspace Title style in ProjectLateralMenu.
[33m54a29b1[m change styles to get uniform backgrounds colors
[33ma747241[m fixed any user could see blocked tasks view. This maybe could change later
[33mff4dad8[m fixed height of project settings
[33m7e2047d[m added TaskListFilter component -with search input and tasks props filters (all grouped in the new component 'TaskPropFIlters'-. Added BlockedTaskView. Moved all search/filter implementations to the TaskListFilter component
[33m82b38a5[m changed/streamlined project's lateral menu navigation methods
[33m42e00d7[m fixed auto-assign-btn not showing for  moderator or workspace's owner
[33ma9be079[m added new UserWorkspaceRole -ROLE_COLLABORATOR-, used mainly for users who can assign any task for themself. Added the ability to add/remove collaborators in Project Settings. Added new checkers in jwtUtils for collaborators. Added new endpoint to autoassign task. Added Auto-Assign BTN in TheTask component. Added 'collaborators' prop to Worspace model, WorkspaceDTO and mappers.
[33m8312f23[m fixed some console error bugs
[33mf6ed463[m fixed some AllTaksView's filters related bugs
[33m72f65df[m added filters to All tasks views (filters for every task's props)
[33mb660240[m added a tag remover on TheTask component
[33mf44fbf2[m fixed ProjectLaturalMenu hover on designated tasks showing forbidden cursor
[33m50da332[m fixed AddTagModal not cleaning tags array
[33m7af130c[m added date utils to generate date templates in dd/MM/YYYY hh:mm am/pm format -for now used in ProjectInfo and TheTask's task updates. Fixed project info width.
[33m66dd895[m fixed ProjectLateralMenu overflow related bugs.
[33mc6305e5[m fixed AddDependendencyModal overflow. Added text input for filter tasks in AddDependencyModal. Fixed broken implementation of AddTagModals. Added new tags added to tag_pool state in localstorage.
[33m0532c67[m poloshed project lateral menu styles
[33m612d0d5[m added remove user control component. Now moderators and workspace's owners can remove users from workspace. Added reactive response to upgrade data whenever an mod or owner modify project settings -or add/remove users/mods- in project settings component.
[33m85b052e[m added more authorization checkers in client for every task modification action. Now task's owner/creator can modify some tasks values. For example this will be usefull to report bugs
[33madb698c[m added project settings layout and divide the component in various parts. Now it has a navigation menu.
[33m0e88e30[m added name and avatar change options in project settings. Added workspaceUtils in client.
[33mc3ddbf3[m added BaseEditDescription reutilizable component (between Task description and workspace description). Removed TaskEditDescription for this new reutilizable component
[33m92406b9[m added endpoints to update workspace name, avatar and description
[33m018d220[m added download json btn in project settings
[33m94e5d71[m added delete workspace btn. For now is disabled because I'm stupid and I have not implemented backups yet
[33m7d748a2[m added a dinamic checker to control the visualization of project control by role in project lateral menu.
[33m04285db[m added a few more styles
[33mf81091b[m fixed any user can see 'invite user' option listed in 'Users' in project lateral menu
[33m3e81cd3[m added project settings component. Now owners can add and remove moderators. All tasks's actions are filtered by authorization. Added project control panel in project lateral menu component. Added invite user by email modal and btn.
[33me608554[m added a setInterval to refresh jwt token every few minutes. It will track moderators and designated tasks to update client-data if values has discrepancies
[33m43d3537[m added WorkspaceDtoWithJWTResponse class to easyly updated user roles after the creation of a new project. Added a 'regenerateToken' method in JwtUtils class to regenerate and update a token from an old token
[33ma66a54a[m fixed task issues generating long int incompatible with javascript when creating task -was using another method-.Removed gson dependency. Changed gson dependency for jackson spring dependency.
[33mec56146[m fixed project lateral menu selected item not changing bg-color as intendeed.
[33mdd4a776[m added custom favicon
[33m782da45[m added ChangeCardSizeBtn component
[33md6645de[m added TaskFilterInput reutilizable component -to filter tasks by text input-
[33m0fab308[m polished lateral menu styles. Added theme checker in main component to prevent theme change after client's manual refresh
[33m247a559[m fixed weird lateral menu behavior, fixed task's issues text not legible in some themes at new task modal. Added role checkers in auth utils
[33m0325fd5[m changed lateral menu and project lateral menu styles. Changed jwt expiratarion time.
[33m6422e2e[m fixed logout bugs -workspaces and task data continue to show after logout-. Added better styles to app lateral menu. Fixed lateral menu weird behavior.
[33m0fe4c8d[m added a custom role/authorization implementation for the api
[33m6bff985[m fixed task's progress item id generation to limit the max value to a value compatible with javascript's limitations
[33m09845cb[m Fixed wrong project lateral menu when accesing a task from home layout component -current project wasn't updating-. Fixed auto switch between project with multiple api calls in a second.
[33mbff548f[m fixed hierarchy icon show when tasks doesn't have a dependency
[33mfcbba79[m added AllTaskView component
[33mfc0c023[m fixed designated tasks cards not updating theme change. Fixed home's designated tasks not refreshing after added/removed self as a designated user for such task
[33m19d41ab[m added desiganted tasks view with different functiontaly according to the app's context. Inside a project/workspace, it only will show user's designated tasks in that project. Outside of the project will show all designated tasks.
[33md38f267[m fixes text color in some components not legible with different themes
[33mdbf5fba[m polished theme selector and themes overall to sustain a style between dark and light themes -card's background are always lighter-
[33mb36be47[m added a theme preview on dropdown menu options
[33mb4bf144[m remove some ugly themes. Added a local storage to the picked theme to get it after refreshing the page
[33m19f4cdc[m added theme changer. Changed various modals/menus colors to make it easy to change between themes without changing styles manually
[33m9a404cf[m fixed user can't create more than  one workspace
[33mc0febfa[m fixed task's depencencies unique constraint in db
[33m8b94fe9[m fixed project lateral menu overflowing
[33mac72111[m fixed card color/progress/priority color not updating whenever the task list is refreshed
[33m59a80b3[m fixed modal reopened after manually close
[33m3d4fdd9[m added task type in props -removed by accident-
[33m2db2e75[m removed drag and drop implementation -well, I just commented- because it was getting overcomplicated. Added local ui-btn-size variable to restore user preferrence upon navigation between tasks and scurm board
[33m7c3f144[m added drag and drop to task in scrum board view
[33m4a3f099[m added micro card view in minicard component. Added change card size btn in scrum view component. Added Create new task btn in scrum view componenent.
[33madfeaf9[m added search/filter(by title/tags) implementation to the scrum view component
[33mc7d3945[m fixed lateral project menu not updating after create new task. Fixed scrum view doesnt erasing columns array after update
[33m27ba3c7[m added 'Testing' Task.Status. Changed lateral menu component to autohide and show on hover. Removed DOCUMENTATION task types in scrum view.
[33m07cab88[m added first implementation of scruw view component
[33mb9cbb5d[m fixed description showing a single block of text. Now it will show paragraphs.
[33m598ee93[m added create/delete task's issues methods and components. Fixed API generating longs UUIDs for issues -longer than supported by js-
[33m9ade9ca[m remove double check input element in task issue component to avoid redundances, now the task will be updated whenever the user check or uncheck the checkbox. Added createTaskIssue and removeTaskIssue endpoints and service's methods to enlarge client's ux working with issues
[33m0467007[m added task issue updater component. Fixed progress itemclass id number longer than 64 bits -which brings problems in typescript/javascript-
[33m7b3d685[m fixed project lateral menu not refreshing
[33mb8b4ccf[m refactor api routes
[33mf37f1ec[m added task description updatetable component
[33m4f5eafd[m added task subtitle editable component
[33m1dd6006[m added task title editable component.
[33m33a9c56[m added method in task service to update title, subtitle, description and task issues. Added id prop to ProgressItem -aka issue- entity.
[33md0a8210[m added a reutilizable component -TaskCommonSelectable- to update Status, Effort and task type
[33m2d8a04c[m added task priority selectable component and 'dedicated' endpoint to updated it
[33m0642c0a[m fixed a bug with page not showing the correct layout after login -needed a f5-.
[33mbd27741[m fixed task not updating after post a new task update
[33m21e090e[m added delete task modal and its implementation
[33m1da442f[m added task update modal component and the ability to post task updates. These updates will auto update in the TheTask component
[33m44f9b2a[m fixed already picked tasks as dependencies showing in the add dependency modal's tasks list
[33mf0cd5c1[m added the ability to add dependencies to tasks -AddDependencyModal component-
[33mb8c6464[m fixed task's names clipping in project lateral menu
[33m85a9877[m added add tag modal. Now users can add tags to tasks
[33m1454dd2[m added the ability to remove task's designated users. Added remove user modal.
[33m6f0968d[m added add user to task modal
[33me4ceae3[m added the ability to download a project as a JSON
[33mba3011c[m added add user by email to project endpoint.
[33mccbef0c[m added task control side menu component.
[33m495530a[m added fontawesome icons lib
[33m965b031[m polished style in TheTask component
[33m7f4c2ab[m fixed task list and lateral menu's task list dont updating after new task creation. Changed minicard task component layout to adjust to long titles.
[33mf44bbc4[m added visual feedback to selected option in menues
[33m1441c35[m changed some styles on TheTask component. Added task navigation in project's lateral menu.
[33ma340c01[m fixed horizontal overflow
[33m8f48294[m polished app layout. Tasks will perfectly show in project lateral menu task section.
[33mf62b7ab[m fixed lateral menu not showing after f5. Added visual feedback to color choose in new task modal.
[33m13f2727[m added color_font css classes to style cards. Added Task Utils. Added a bunch of new color in the Color enum.
[33mba1f13e[m deleted some props in TaskLite class. TaskLite now have only the props usesful to visualization/order tasks lists
[33mb110870[m refactor api to fetch tasks by workspace from task repo. Added task list and task mini card first implementation.
[33m416f03c[m added create task modal form with success and fail visual feedback and new task button component. Added reutilizable base modal. Fixed some typos in client types. Added ProgressItem interface to client.
[33m02e53dd[m added login logout buttons on home componenent. Added auth utils to manage jwt token expiration, and updated login-logout flow to updated related component auth state.
[33mc437457[m changed styles in project info users list.
[33m7ebcedd[m changed lateral menu style.
[33m6b70bda[m Some refactor. Moved home functionality to App.vue. Added Project lateral menu component. Added Project info component.
[33ma6b8ee8[m added project main view. Fixed project_avatar prop not showing.
[33m5d845d8[m added completprofile and first project components to the sign up flow. Changed patch method of apiCall composable to adjust to axios parameters. Added store for projects.
[33m03ded61[m added register component with steps -user, profile, project-. Added avatar prop to the workspace entity in the api
[33maee9eb8[m added main layout with lateral menu and home view
[33m2722938[m added vue router, added reroute after login to home component.
[33m6eddc93[m added dracula daisyui theme, added login form. Added axios interceptor for JWT requests. Added user store.
[33mab8515d[m added progress item prop to task entity. Its a list of the class ProgressItem. Its have two props, a string and a boolean. Its a utility for add checklists to the cards in the frontend.
[33m010a18b[m fixed authentication requests with jwt doesnt working
[33m43970d3[m fixed login auth doesnt return jwt
[33mae6f371[m added jwt utils, login response utility class and RSAkey properties
[33m7ed9ef7[m registration and login works without JWT.
[33m369a8b4[m added user roles. Added CustomUserDetails, UsernamePwdAuthenticationProvider and JwtGenerator and JWT validator filter classes.
[33md769026[m added more endpoints to get specific user's related entities
[33mb705b99[m fixed task card colour don't changing
[33m0614244[m added pinia store. Added first example of task card. Added basic cors configuration to the API. Added classes models in client to task, user, workspace, etc.
[33m78bb2de[m added useApiCall composable for modular api calls -with endpoint enum-
[33m4fcd494[m fixed task_update class structure. Removed @Entity and make it embedabble. Now the API will generated a random ID for tasks.
[33m0520b40[m clean some comments
[33m3e618d3[m added the ability to add/remove tasks's tags and tasks updates -task updates are updatable too-
[33mdced6af[m added the ability to assign/unassign user's to/from tasks.
[33me6b0577[m added TwoTask class helper. Using it, now the user can add and remove dependencies to/from tasks.
[33m7bf5875[m now users can be added as moderators in workspaces they are members
[33m3639caf[m added the ability to add or remove users from a workspace
[33md6404db[m debugging until first postman's tests passed.
[33mc62641b[m Added user and workstation services, services implementation and controllers. Added Invalid user and workspaces exceptions.
[33md84576d[m added dtos's lite versions. Added user and workspace mappers. Added user and workspaces repositories
[33m056f278[m added taskdto, task mapper and user and workspace entities model
[33mc9226ff[m soy un gil
[33m570063f[m hi world+
