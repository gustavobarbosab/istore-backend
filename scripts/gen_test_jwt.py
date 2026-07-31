#!/usr/bin/env python3
"""
Generates an RS256 JWT signed with the local private key (private.pem),
just to test the Gateway routes that require authentication.

This stands in for a real auth server, which doesn't exist yet in this
exercise. Once a real auth service exists, this key/JWK should be
replaced with theirs and this script discarded.

Do NOT commit private.pem in a real repo outside this study exercise.

Usage:
    python3 gen_test_jwt.py [subject] [ttl_seconds]
"""
import sys
import time
import jwt  # pip install pyjwt

KEY_PATH = "private.pem"
KID = "gateway-local-dev-1"


def main():
    subject = sys.argv[1] if len(sys.argv) > 1 else "test-user-123"
    ttl = int(sys.argv[2]) if len(sys.argv) > 2 else 3600

    with open(KEY_PATH, "rb") as f:
        private_key = f.read()

    now = int(time.time())
    payload = {
        "sub": subject,
        "iat": now,
        "exp": now + ttl,
    }

    token = jwt.encode(
        payload,
        private_key,
        algorithm="RS256",
        headers={"kid": KID},
    )
    print(token)


if __name__ == "__main__":
    main()
