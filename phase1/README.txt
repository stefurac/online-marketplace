CSC207H1 F - Software Design - Group_0025 Project Phase 1

Group Members:
Ran (Megan) Shi
Yin (Luke) Liu
Cassandra Kate Stefura
Peter Boulos
Chenhao (William) Wang
David Anugraha
Wei Wei
Monika Dydynski
------------------------------------------------------------------------------------------------------------------------
README.txt
Any instructions needed to run our program and try out the required functionality.

In IntelliJ, clone Group repository URL https://markus.teach.cs.toronto.edu/git/csc207-2020-05/group_0025.
Run 'Main' from phase1 folder and follow the prompts on the screen.
------------------------------------------------------------------------------------------------------------------------
Usage:

Login and Registration Guide:
    Admin Login
        The following administrative user is preloaded in this system
            username: admin
            password: admin
        You must login as the preloaded admin to add subsequent administrative users to the system. Follow the prompts
        on the screen to login the preloaded admin.
    User Login & Registration
        Follow the prompts on the screen to login an existing user.Again, note that administrative accounts are
        registered by existing admin users - the system generates a random password for new admin users.
------------------------------------------------------------------------------------------------------------------------
Admin Guide:

We have decided that an admin cannot interact with our system in the same way as a user (i.e. does not maintain a
wishlist, inventory, cannot participate in trades)
Upon logging in, an administrative user can:
1. View and manage their profile:
    1. Change username
	2. Change password
2. Manage user accounts:
    1. View all users in the system
	2. View and manage flagged users (users who have been flagged by the system for violating threshold rules) by:
        1. An admin can freeze a flagged user, or
        2. Unflag a flagged user
    3. View and unfreeze frozen users
    4. View and unfreeze frozen users who have requested to be unfrozen
3. Manage admin accounts:
    1. View existing admin and register subsequent admin (the existing admin inputs a username and the system generates
    a password for the new admin account)
4. Manage items in the system:
    1. View all items that are not pending trade and select to remove an item from the system (and thereby the user's
    inventory) at the admin's discretion
    2. View items that have been added by users that are waiting for approval. An admin can accept or reject an item
    from being added to the user's inventory (and to the system, as a result)
5. Manage system thresholds:
    An admin can edit the following thresholds:
    1. meeting cancellation threshold
    2. weekly trade limit (the number of trades an admin is allowed to participate in weekly)
    3. borrowed-lent difference threshold (the number of times a user must lend more than borrow)
    4. meeting edits threshold (the number of times a user is allowed to edit a meeting before the trade is cancelled)
    5. minimum password length (the minimum number of characters that a password must be upon user registration)
6. Log out (ends the session for the admin)
------------------------------------------------------------------------------------------------------------------------
User Guide:

A user is any account on the system via which a user of the system may maintain their own profile, browse existing items
on the system, and initiate and proceed through a trade.
The following are the capabilities of a user:
1. Manage Profile:
    1. View profile, including username, user ID, and current status
    2. Change Username
    3. Change Password
    4. Request to be unfrozen (if currently frozen); then an admin may view the request and decide whether to restore
    good-standing status for this user
2. Manage Items
    1. View all items on the system
    2. Upload a new item to his/her inventory that much be approved by an admin
    3. View his/her inventory
    4. View his/her wishlist
3. Manage Trades
    1. Display the trades he/she is involved in, displayed by certain categories
    2. Display recent items traded or common traders
    3. Request a new one-way or two-way trade
    4. Manage his/her trades
4. Log Out

User Status Guide:

GOOD: a user that is in good-standing; they are allowed to request and perform trade
FLAGGED: a user is flagged automatically by the system for violating thresholds, and pending for review by an admin; a
flagged user is prevented from initiating/receiving trades
FROZEN: a user is was previously flagged by the system. Frozen status is confirmed by an admin. A frozen user is one who
cannot initiate or receive trades
REQUESTED: a frozen User that has requested to be unfrozen, and pending for review by an admin. A requested user is
still one that is frozen and are thereby prevented from initiating/receiving trades

------------------------------------------------------------------------------------------------------------------------
Trade Status Guide:

REQUESTED: a Trade that has been requested by a User and that invites another User to a one- or two-way Trade
ACCEPTED: a Trade that has been accepted by the User who received a request Trade
REJECTED: a Trade that has been rejected by the User who received a request Trade
CONFIRMED: a Trade in which a Meeting time and place has been confirmed by both the Trade requester and the receiver
of a Trade request
REQUESTERCONFIRMED: a Trade in which the requester of the initial Trade request confirms that the Trade has taken place
in real life
RECEIVERCONFIRMED: a Trade in which the receiver of the initial Trade request confirms that the Trade has taken place
in real life
COMPLETED: a Trade in which both parties associated with a Trade confirm that the Trade has taken place in real life
CANCELLED:  a Trade that has been cancelled after it was accepted by the receiver of the Trade request due too many
edit counts for the Meeting

Trade and Meeting Guide:

From the User options, Users may access Trade options.

> If a User does not have a Good status, the User can only display a Trade
> If a User has a Good status, the User can display a Trade, request, accept, reject, and confirm a Trade, in addition
to editing and confirming a Meeting for a particular Trade.

1. Displaying a Trade

The Users have the options to view all of the Trades in which they borrow an Item from another User,
lend an Item to another user, two-way Trades, requested Trades, accepted Trades, rejected Trades,
cancelled Trades, confirmed Trades, completed Trades, or to view all of the Trades that they have participated in.

They also have the option to view the most recent Items they have lent, the most recent Items that they have borrowed,
the most recent Items in which they have made a two-way Trade, or they can view their most frequent trading
partners.

2. Requesting a Trade

In addition on having a Good status, if a User does not have too many cancelled Trades, and has not reached the
Trade limit for this week, they may choose between the all of the above viewing options that a non-Good User may choose
between, as well as the ability to request a one- or two-way Trade.

To discover possible Items to make Trades with, the User can go into the Item options. From there, the User must note
the username of the User that owns the Items that the User is interested in trading.

In order to request a Trade, a User must input the username of the other User that they are interested in making a
Trade with. Note that it is required that the User is able to recall the other User in order to request a Trade.

They may request a one-way Trade if the User has not borrowed more Items than they have lent and, in the case that they
can borrow, the requested trading partner has the Item they want to borrow and it is available.

They may request a two-way Trade if the requested trading partner has the Item that they are requesting to borrow and
it is available and the User has the Item they want to lend and it is available. Once a User successfully requests a
two-way Trade, their desired lending Item becomes unavailable.

When a User requests any type of Trade, they must decide whether they want to request a permanent Trade or they want to
request a Trade in which both participants return the Items that are being Traded 30 days later.

The User cannot spam requests with the same Item repeatedly.

The User can also manage their Trades by entering the trade ID (which trades can be displayed from option (1).
Our system will automatically give you the possible options to do with a Trade

3. Accepting and rejecting a Trade

A User, who is a receiver of a request Trade, may accept or reject a Trade if the status of the Trade is requested.

If the User rejects a Trade and it is a two-way Trade, the requester's Item will be available again.

If the receiver of the Trade request does not have too many cancelled Trades, and has not reached the Trade limit for
this week, they may accept a requested Trade.

After a Trade is accepted, a Meeting is initiated and the User who accepted the Trade request must suggest a time and
place for both Users who are participating in the Trade to meet, and the User still need to confirm the Meeting (later).
The receiver's Item becomes Unavailable.

4. Editing and confirming a Meeting

A User may edit or confirm a Meeting if the Trade status is Accepted.

In editing a Meeting, Users will have to provide a time and place (unless it is a returning Trade, where you can only
provide a place to meet). After a User edit a meeting, there will be a change of turns, where the current User may not
edit the Meeting and the other User can edit the Meeting. There will be a maximum of edit threshold (of 3 currently,
can be changed), and once the threshold is exceeded, the Trade is automatically cancelled.

If a User confirms a Meeting time and place that has been suggested by another User, that is, both the
Trade request receiver and the Trade requester have confirmed a Meeting time and place, a Meeting is confirmed.

5. Confirming a Trade

The User confirms a Trade when the Item(s) in Trade has been exchanged. If both Users confirm a Trade, then the Trade
is called completed.

For a permanent completed Trade, the Item(s) become(s) Available again. For a temporary completed Trade, there will
be a new Trade where the Meeting time (currently 30 days from the day when the Meeting time is agreed) may not be
changed with the same place as the previous Meeting. If the Trade is cancelled (due to excessive number of edits),
both Users will be (flagged to be) investigated by Admins and the Item(s) will be Available again.
------------------------------------------------------------------------------------------------------------------------
Item Status Guide:

UNCHECKED: this item is uploaded into the system, but have not been approved by the administrator; other user can add
this item to their inventory but not request trade
AVAILABLE: this item is approved by the administrator and is available for trade in the system
UNAVAILABLE: this item is unavailable for trade and cannot be edited


Item Guide:

1. The User is able to view all items in the system
2. The User can view and edit inventory list and wish list
3. The User can upload new item to their inventory, which has status unchecked.

------------------------------------------------------------------------------------------------------------------------