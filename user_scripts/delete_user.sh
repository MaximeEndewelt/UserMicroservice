echo "THIS SCRIPT IS DELETING USER 5"

echo "USER 5 : "
curl -H "Content-Type: application/json" -w "%{http_code}" -X DELETE http://localhost:8585/user/scott@gmail.com






echo "\nEND OF USER DELETION"
exit 1

