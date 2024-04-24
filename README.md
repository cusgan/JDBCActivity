<h1>MyJournal</h1>
<p>An application for users to write and keep a private digital journal.
Uses JavaFX and Java Database Connectivity.</p>

<h3>Tables</h3>
<table>
  <tr>
    <th colspan="4">User</th>
    <th>CRUD</th>
  </tr>
  <tr>
    <td><b>id</b></td>
    <td>username</td> 
    <td>password</td> 
    <td>entrycount</td>
    <td>create user on signup, read username and entry count, update entry count on entry creation, delete user</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan="5">Entry</th>
    <th>CRUD</th>
  </tr>
  <tr>
    <td><b>entryid</b></td>
    <td>userid (fk)</td> 
    <td>dateposted</td> 
    <td>title</td>
    <td>content</td>
    <td>create entry, read entries by logged in user, update entry title &/ content, delete entry</td>
  </tr>
</table>
